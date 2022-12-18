package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.OnlineTxn;
import com.infinitekind.moneydance.model.OnlineTxnList;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.Transaction;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * The handler class converts incoming transactions after a successful service
 * call. The same instance can be used for multiple service calls and returns
 * a cumulative result list of <code>OnlineTxn</code>s.
 */
final class TransactionSearchRequestHandler
extends AbstractRequestHandler<PaymentTransactionSearchResultType> {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            TransactionSearchRequestHandler.class.getName());

    private static final BigDecimal MULTIPLIER = BigDecimal.valueOf(100);

    private final Account account;
    private final OnlineTxnList txnList;
    private final DateFormat dateFormat;

    TransactionSearchRequestHandler(
            final ViewController argViewController,
            final IAccountBook accountBook,
            final String argAccountId,
            final DateFormat argDateFormat,
            final Localizable argLocalizable) {

        super(argViewController,
                argLocalizable);
        this.account = accountBook.getAccountById(argAccountId);
        this.txnList = accountBook.getRootAccount().getDownloadedTxns();
        this.dateFormat = argDateFormat;
    }

    @Override
    public void serviceCallSucceeded(
            final ServiceResult<PaymentTransactionSearchResultType>
            serviceResult) {

        final List<PaymentTransactionSearchResultType> txns =
                serviceResult.getResults().orElseThrow(AssertionError::new);

        final List<OnlineTxn> resultList =
                new ArrayList<>(txns.size());
        long startDateLong = Long.MAX_VALUE;

        for (PaymentTransactionSearchResultType result : txns) {
            try {
                resultList.add(this.createNewOnlineTxn(result));
                long timestamp = this.dateFormat.parse(
                        result.getTimestamp()).getTime();
                if (timestamp < startDateLong) {
                    startDateLong = timestamp;
                }
            } catch (ParseException e) {
                final String message = e.getMessage();
                if (message != null) {
                    LOG.log(Level.WARNING, message, e);
                }
            }
        }

        this.getViewController().transactionsImported(
                resultList,
                new Date(startDateLong),
                this.account,
                serviceResult.getErrorCode().orElse(null));
    }

    private OnlineTxn createNewOnlineTxn(
            final PaymentTransactionSearchResultType result)
                    throws ParseException {

        final String transactionID = result.getTransactionID();
        String grossAmount = "0";
        CurrencyCodeType currencyID = null;
        if (result.getGrossAmount() != null) {
            grossAmount = result.getGrossAmount().getValue();
            currencyID = result.getGrossAmount().getCurrencyID();
        }

        LOG.info(String.format(
                "Timestamp      : %s", result.getTimestamp()));
        LOG.info(String.format(
                "Gross Amount   : %s %s", grossAmount, currencyID));
        LOG.info(String.format(
                "Payer Display  : %s", result.getPayerDisplayName()));
        LOG.info(String.format(
                "Payer          : %s", result.getPayer()));
        LOG.info(String.format(
                "Status         : %s", result.getStatus()));
        LOG.info(String.format(
                "Transaction ID : %s", result.getTransactionID()));
        LOG.info(String.format(
                "Type           : %s", result.getType()));
        LOG.info("");

        String payer = "";
        if (result.getPayer() != null) {
            payer = String.format("%s ", result.getPayer());
        }

        final String memo = String.format("%s%s %s %s",
                payer,
                result.getStatus(),
                result.getTransactionID(),
                result.getType());

        final long amount = new BigDecimal(grossAmount).multiply(
                MULTIPLIER).longValueExact();

        final String description = result.getPayerDisplayName();

        final long date = this.dateFormat.parse(
                result.getTimestamp()).getTime();

        return new Transaction(
                txnList,
                transactionID,
                amount,
                description,
                memo,
                date);
    }
}
