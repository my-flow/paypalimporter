// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.Util;
import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.apps.md.model.OnlineTxnList;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
final class TransactionSearchIterator implements ViewController {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            TransactionSearchIterator.class.getName());

    private static final long ONE_SECOND = 1000;
    private static final String KEY_ACCOUNT_URL = "account_url";

    private final ViewController viewController;
    private final RootAccount rootAccount;
    private final ServiceProvider serviceProvider;
    private final InputData inputData;
    private final CurrencyType currencyType;
    private final CurrencyCodeType currencyCode;
    private final Date startDate;
    private final RequestHandler<PaymentTransactionSearchResultType>
    requestHandler;
    private final List<OnlineTxn> resultList;

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

        this.startDate = DateUtils.truncate(
                Util.convertIntDateToLong(
                        this.inputData.getDateRange().getStartDateInt()),
                        Calendar.DATE);
        this.requestHandler = new TransactionSearchRequestHandler(
                this,
                this.rootAccount);
        this.resultList = new ArrayList<OnlineTxn>();
    }

    void callTransactionSearchService() {
        final Date endDate = DateUtils.ceiling(
                Util.convertIntDateToLong(
                        this.inputData.getDateRange().getEndDateInt()),
                        Calendar.DATE);
        this.callTransactionSearchService(endDate);
    }

    @Override
    public void transactionsImported(
            final List<OnlineTxn> argOnlineTxns,
            final Date argStartDate,
            final Account argAccount,
            final String errorCode) {

        this.resultList.addAll(argOnlineTxns);

        if (Settings.getErrorCodeSearchWarning().equals(errorCode)) {
            final Date endDate = new Date(argStartDate.getTime() - ONE_SECOND);
            LOG.info(String.format("Imported until:  %s", argStartDate));
            LOG.info(String.format("Next start date: %s", this.startDate));
            LOG.info(String.format("Next end date:   %s", endDate));
            this.callTransactionSearchService(endDate);
        } else {
            LOG.info("All transactions downloaded");
            this.inputData.getPassword(true);

            final Account useAccount = findOrCreateAccount(
                    this.rootAccount,
                    this.inputData.getAccountId(),
                    this.currencyType);
            final OnlineTxnList txnList = useAccount.getDownloadedTxns();
            final ListIterator<OnlineTxn> iter = argOnlineTxns.listIterator(
                    argOnlineTxns.size());

            while (iter.hasPrevious()) {
                txnList.addNewTxn(iter.previous());
            }

            this.viewController.transactionsImported(
                    argOnlineTxns, null, useAccount, null);
        }
    }

    private void callTransactionSearchService(final Date endDate) {

        this.serviceProvider.callTransactionSearchService(
                this.inputData.getUsername(),
                this.inputData.getPassword(false),
                this.inputData.getSignature(),
                this.startDate,
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
            LOG.info("Creating new account");
            // ESCA-JAVA0166: Account.makeAccount throws generic exception
            try {
                account = Account.makeAccount(
                        Account.ACCOUNT_TYPE_BANK,
                        Helper.getLocalizable().getNameNewAccount(),
                        currencyType,
                        rootAccount);
                account.setParameter(
                        KEY_ACCOUNT_URL,
                        Helper.getLocalizable().getUrlNewAccount());
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
        // ignore
    }

    @Override
    public void startWizard() {
        // ignore
    }

    @Override
    public void cancel() {
        // ignore
    }

    @Override
    public void proceed() {
        // ignore
    }

    @Override
    public void unlock(final String text, final Object key) {
        // ignore
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
        // ignore
    }

    @Override
    public void refreshAccounts(final int accountId) {
        // ignore
    }
}
