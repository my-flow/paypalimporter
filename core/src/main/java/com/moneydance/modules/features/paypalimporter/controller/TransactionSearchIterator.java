package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.OnlineTxn;
import com.infinitekind.moneydance.model.OnlineTxnList;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Logger;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

import javax.annotation.Nullable;

/**
 * This controller class fetches the transactions iteratively by adjusting
 * the date bounds and calling the transaction search service. It aggregates
 * the results and imports the transactions all at once.
 *
 * The PayPal API delivers not more than 100 transactions per service calls.
 * If there are more than 100 transactions available, the PayPal API will
 * return the newest 100 transactions together with a search warning.
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

    private final ViewController viewController;
    private final IAccountBook accountBook;
    private final ServiceProvider serviceProvider;
    private final InputData inputData;
    private final CurrencyType currencyType;
    private final CurrencyCodeType currencyCode;
    private final RequestHandler<PaymentTransactionSearchResultType>
    requestHandler;
    private final Set<OnlineTxn> resultSet;
    private final String errorCodeSearchWarning;
    private final Localizable localizable;

    @SuppressWarnings("initialization")
    TransactionSearchIterator(
            final ViewController argViewController,
            final IAccountBook argIAccountBook,
            final ServiceProvider argServiceProvider,
            final InputData argInputData,
            final CurrencyType argCurrencyType,
            final CurrencyCodeType argCurrencyCode,
            final String argErrorCodeSearchWarning,
            final DateFormat argDateFormat,
            final Localizable argLocalizable) {

        this.viewController = argViewController;
        this.accountBook = argIAccountBook;
        this.serviceProvider = argServiceProvider;
        this.inputData = argInputData;
        this.currencyType = argCurrencyType;
        this.currencyCode = argCurrencyCode;

        this.requestHandler = new TransactionSearchRequestHandler(
                this,
                this.accountBook,
                argInputData.getAccountId().orElse(null),
                argDateFormat,
                argLocalizable);
        this.resultSet = new LinkedHashSet<>();
        this.errorCodeSearchWarning = argErrorCodeSearchWarning;
        this.localizable = argLocalizable;
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
            @Nullable final String errorCode) {

        this.resultSet.addAll(argOnlineTxns); // aggregate all results

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
                    "All %d transactions downloaded", this.resultSet.size()));

            account = findOrCreateAccount(
                    this.accountBook,
                    this.inputData.getAccountId().orElse(null),
                    this.currencyType);
            final OnlineTxnList txnList = account.getDownloadedTxns();

            // import all transactions in reverse order,
            // i.e. from oldest to newest
            final Iterator<OnlineTxn> iter =
                new LinkedList<>(this.resultSet).descendingIterator();
            while (iter.hasNext()) {
                txnList.addNewTxn(iter.next());
            }
        }

        this.viewController.transactionsImported(
                new LinkedList<>(this.resultSet), argStartDate,
                account, errorCode);
    }

    @Override
    public void unlock() {
        this.viewController.unlock();
    }

    @Override
    public void unlock(
            @Nullable final String text,
            @Nullable final Object key) {
        if (!this.errorCodeSearchWarning.equals(key)) {
            this.viewController.unlock(text, key);
        }
    }

    /**
     * @param endDate The adapted end date which must be an earlier date than
     * the previous end date.
     */
    @SuppressWarnings("nullness")
    private void callTransactionSearchService(final Date endDate) {

        this.serviceProvider.callTransactionSearchService(
                this.inputData.getUsername().orElseThrow(AssertionError::new),
                this.inputData.getPassword(false).orElseThrow(AssertionError::new),
                this.inputData.getSignature().orElseThrow(AssertionError::new),
                this.inputData.getStartDate(),
                endDate,
                this.currencyCode,
                this.requestHandler);
    }

    private Account findOrCreateAccount(
            final IAccountBook argAccountBook,
            final String argAccountId,
            final CurrencyType argCurrencyType) {

        Account account = argAccountBook.getAccountById(argAccountId);
        if (account == null) {
            // lazy creation of a Moneydance account if none has been given
            LOG.info("Creating new account");
            account = this.accountBook.createBankAccount(
                    this.localizable.getNameNewAccount(),
                    argCurrencyType,
                    this.localizable.getUrlNewAccount());
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
    public void proceed(final InputData argInputData) {
        this.viewController.proceed(argInputData);
    }

    @Override
    public void currencyChecked(
            final CurrencyType argCurrencyType,
            final CurrencyCodeType argCurrencyCode,
            final List<CurrencyCodeType> argCurrencyCodes) {
        // ignore
    }

    @Override
    public void showHelp() throws MalformedURLException {
        this.viewController.showHelp();
    }

    @Override
    public void refreshAccounts(final String accountId) {
        this.viewController.refreshAccounts(accountId);
    }
}
