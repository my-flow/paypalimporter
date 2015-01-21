// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.apps.md.model.OnlineTxnList;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * This controller class fetches the transactions iteratively by adjusting
 * the date bounds and calling the transaction search service. It aggregates
 * the results and imports the transactions all at once.
 *
 * The PayPal API delivers not more 100 transactions per service calls. If there
 * are more than 100 transactions available, the PayPal API will return the
 * newest 100 transactions together with a search warning.
 *
 * This class issues consecutive service calls. It starts with the newest 100
 * transactions and calls the PayPal API repeatedly. It moves the date bounds
 * into the past more and more. The received transactions are collected in a
 * result list where the transactions received by the latest service call are
 * appended, so the list contains the transactions in reverse order, from
 * newest to oldest.
 *
 * Finally the result list gets reversed and attached the Moneydance account.
 *
 * @author Florian J. Breunig
 */
final class TransactionSearchIterator implements ViewController {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            TransactionSearchIterator.class.getName());

    private static final String KEY_ACCOUNT_URL = "account_url";

    private final ViewController viewController;
    private final RootAccount rootAccount;
    private final ServiceProvider serviceProvider;
    private final InputData inputData;
    private final CurrencyType currencyType;
    private final CurrencyCodeType currencyCode;
    private final RequestHandler<PaymentTransactionSearchResultType>
    requestHandler;
    private final List<OnlineTxn> resultList;
    private final String errorCodeSearchWarning;

    TransactionSearchIterator(
            final ViewController argViewController,
            final RootAccount argRootAccount,
            final ServiceProvider argServiceProvider,
            final InputData argInputData,
            final CurrencyType argCurrencyType,
            final CurrencyCodeType argCurrencyCode) {

        Validate.notNull(argViewController, "view controller must not be null");
        Validate.notNull(argRootAccount, "root account must not be null");
        Validate.notNull(argServiceProvider,
                "service provider must not be null");
        Validate.notNull(argInputData, "input data must not be null");
        Validate.notNull(argCurrencyType, "currency type must not be null");
        Validate.notNull(argCurrencyCode, "currency code must not be null");
        Validate.notNull(argRootAccount, "root account must not be null");
        this.viewController = argViewController;
        this.rootAccount = argRootAccount;
        this.serviceProvider = argServiceProvider;
        this.inputData = argInputData;
        this.currencyType = argCurrencyType;
        this.currencyCode = argCurrencyCode;

        this.requestHandler = new TransactionSearchRequestHandler(
                this,
                this.rootAccount);
        this.resultList = new ArrayList<OnlineTxn>();
        this.errorCodeSearchWarning =
                Helper.INSTANCE.getSettings().getErrorCodeSearchWarning();
    }

    /**
     * Initiate consecutive service calls.
     */
    void callTransactionSearchService() {
        this.callTransactionSearchService(this.inputData.getEndDate());
    }

    @Override
    public void transactionsImported(
            final List<OnlineTxn> argOnlineTxns,
            final Date argStartDate,
            final Account argAccount,
            final String errorCode) {

        this.resultList.addAll(argOnlineTxns); // aggregate all results

        final Account account;
        if (this.errorCodeSearchWarning.equals(errorCode)) {
            // there are more transactions in the pipeline, so we need to
            // adjust the date bounds and issue another service call.
            LOG.info(String.format("Next start date: %s",
                    this.inputData.getStartDate()));
            LOG.info(String.format("Next end date:   %s", argStartDate));
            account = null;
            this.callTransactionSearchService(argStartDate);
        } else {
            // there are no more transactions in the pipeline, so we can
            // start attaching the transactions all at once.
            LOG.info(String.format(
                    "All %d transactions downloaded", this.resultList.size()));

            account = findOrCreateAccount(
                    this.rootAccount,
                    this.inputData.getAccountId(),
                    this.currencyType);
            final OnlineTxnList txnList = account.getDownloadedTxns();
            final ListIterator<OnlineTxn> iter = this.resultList.listIterator(
                    this.resultList.size());

            // import all transactions in reverse order,
            // i.e. from oldest to newest
            while (iter.hasPrevious()) {
                txnList.addNewTxn(iter.previous());
            }
        }
        this.viewController.transactionsImported(
                this.resultList, argStartDate, account, errorCode);
    }

    @Override
    public void unlock(final String text, final Object key) {
        if (!this.errorCodeSearchWarning.equals(key)) {
            this.viewController.unlock(text, key);
        }
    }

    /**
     * @param endDate The adapted end date which must be an ealier date than
     * the previous end date.
     */
    private void callTransactionSearchService(final Date endDate) {

        this.serviceProvider.callTransactionSearchService(
                this.inputData.getUsername(),
                this.inputData.getPassword(false),
                this.inputData.getSignature(),
                this.inputData.getStartDate(),
                endDate,
                this.currencyCode,
                this.requestHandler);
    }

    private static Account findOrCreateAccount(
            final RootAccount rootAccount,
            final int accountId,
            final CurrencyType currencyType) {

        Account account = rootAccount.getAccountById(accountId);
        if (account == null) {
            // lazy creation of a Moneydance account if none has been given
            LOG.info("Creating new account");
            // ESCA-JAVA0166: Account.makeAccount throws generic exception
            try {
                account = Account.makeAccount(
                        Account.ACCOUNT_TYPE_BANK,
                        Helper.INSTANCE.getLocalizable().getNameNewAccount(),
                        currencyType,
                        rootAccount);
                account.setParameter(
                        KEY_ACCOUNT_URL,
                        Helper.INSTANCE.getLocalizable().getUrlNewAccount());
                rootAccount.addSubAccount(account);
            } catch (Exception e) {
                LOG.log(Level.WARNING, e.getMessage(), e);
                throw new IllegalStateException(
                        "Could not create account", e);
            }
        }
        return account;
    }

    @Override
    public void update(final Observable observable, final Object arg) {
        this.viewController.update(observable, arg);
    }

    @Override
    public void startWizard() {
        this.viewController.startWizard();
    }

    @Override
    public void cancel() {
        this.viewController.cancel();
    }

    @Override
    public void proceed() {
        this.viewController.proceed();
    }

    @Override
    public void currencyChecked(
            final CurrencyType argCurrencyType,
            final CurrencyCodeType argCurrencyCode,
            final List<CurrencyCodeType> argCurrencyCodes) {
        // ignore
    }

    @Override
    public void showHelp() {
        this.viewController.showHelp();
    }

    @Override
    public void refreshAccounts(final int accountId) {
        this.viewController.refreshAccounts(accountId);
    }
}
