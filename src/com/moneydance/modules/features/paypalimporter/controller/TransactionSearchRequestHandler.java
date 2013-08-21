// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.apps.md.model.OnlineTxnList;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 *
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
    private final DateFormat dateFormat;

    TransactionSearchRequestHandler(
            final ViewController argViewController,
            final RootAccount argAccount) {

        super(argViewController);
        Validate.notNull(argAccount, "account must not be null");
        this.account = argAccount;
        this.dateFormat = Helper.INSTANCE.getSettings().getDateFormat();
    }

    @Override
    public void serviceCallSucceeded(
            final ServiceResult<PaymentTransactionSearchResultType>
            serviceResult) {

        final List<PaymentTransactionSearchResultType> txns =
                serviceResult.getResults();
        final OnlineTxnList txnList = this.account.getDownloadedTxns();
        final List<OnlineTxn> resultList =
                new ArrayList<OnlineTxn>(txns.size());
        long startDateLong = Long.MAX_VALUE;

        for (PaymentTransactionSearchResultType result : txns) {
            try {
                resultList.add(this.createNewOnlineTxn(txnList, result));
                long timestamp = this.dateFormat.parse(
                        result.getTimestamp()).getTime();
                if (timestamp < startDateLong) {
                    startDateLong = timestamp;
                }
            } catch (ParseException e) {
                LOG.log(Level.WARNING, e.getMessage(), e);
            }
        }

        this.getViewController().transactionsImported(
                resultList,
                new Date(startDateLong),
                null,
                serviceResult.getErrorCode());
    }

    private OnlineTxn createNewOnlineTxn(
            final OnlineTxnList txnList,
            final PaymentTransactionSearchResultType result)
                    throws ParseException {

        final String fitxnid = result.getTransactionID();
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

        final OnlineTxn onlineTxn = txnList.newTxn();
        onlineTxn.setProtocolType(OnlineTxn.PROTO_TYPE_OFX);
        onlineTxn.setAmount(amount);
        onlineTxn.setTotalAmount(amount);
        onlineTxn.setName(description);
        onlineTxn.setMemo(memo);
        onlineTxn.setFITxnId(fitxnid);
        onlineTxn.setDatePosted(date);
        onlineTxn.setDateInitiated(date);
        onlineTxn.setDateAvailable(date);
        return onlineTxn;
    }
}
