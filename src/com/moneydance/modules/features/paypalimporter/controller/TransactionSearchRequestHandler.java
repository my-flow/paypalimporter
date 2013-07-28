// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.apps.md.model.OnlineTxnList;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
final class TransactionSearchRequestHandler
extends AbstractRequestHandler<PaymentTransactionSearchResultType> {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            TransactionSearchRequestHandler.class.getName());

    private static final BigDecimal MULTIPLIER = BigDecimal.valueOf(100);
    private static final int PROTOCOL_TYPE = OnlineTxn.PROTO_TYPE_OFX;
    private static final String KEY_ACCOUNT_URL = "account_url";

    private final RootAccount rootAccount;
    private final int accountNum;
    private final CurrencyType currencyType;
    private final DateFormat dateFormat;
    private final String nameNewAccount;
    private final String urlNewAccount;

    TransactionSearchRequestHandler(
            final ViewController argViewController,
            final RootAccount argRootAccount,
            final int argAccountNum,
            final CurrencyType argCurrencyType) {

        super(argViewController);
        Validate.notNull(argRootAccount, "root account must not be null");
        Validate.notNull(argCurrencyType,
                "currency type account must not be null");
        this.rootAccount = argRootAccount;
        this.accountNum = argAccountNum;
        this.currencyType = argCurrencyType;
        this.dateFormat = new SimpleDateFormat(
                Helper.INSTANCE.getSettings().getDatePattern(), Locale.US);
        this.nameNewAccount =
                Helper.INSTANCE.getLocalizable().getNameNewAccount();
        this.urlNewAccount =
                Helper.INSTANCE.getLocalizable().getUrlNewAccount();
    }

    @Override
    public void serviceCallSucceeded(
            final ServiceResult<PaymentTransactionSearchResultType>
            serviceResult) {

        Account useAccount = this.rootAccount.getAccountById(
                this.accountNum);
        if (useAccount == null) {
            LOG.info("Creating new account");

            // ESCA-JAVA0166: Account.makeAccount throws generic exception
            try {
                useAccount = Account.makeAccount(
                        Account.ACCOUNT_TYPE_BANK,
                        this.nameNewAccount,
                        this.currencyType,
                        this.rootAccount);
                useAccount.setParameter(KEY_ACCOUNT_URL, this.urlNewAccount);
                this.rootAccount.addSubAccount(useAccount);
            } catch (Exception e) {
                LOG.log(Level.WARNING, e.getMessage(), e);
                throw new IllegalStateException("Could not create account", e);
            }
        }

        List<PaymentTransactionSearchResultType> txns =
                serviceResult.getResults();
        OnlineTxnList txnList = useAccount.getDownloadedTxns();
        for (PaymentTransactionSearchResultType result : txns) {
            try {
                OnlineTxn txn = txnList.newTxn();
                this.handleResultType(txn, result);
                txnList.addNewTxn(txn);
            } catch (ParseException e) {
                LOG.log(Level.WARNING, e.getMessage(), e);
            }
        }
        this.getViewController().transactionsImported(useAccount);
    }

    private void handleResultType(
            final OnlineTxn txn,
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

        txn.setProtocolType(PROTOCOL_TYPE);
        txn.setAmount(amount);
        txn.setTotalAmount(amount);
        txn.setName(description);
        txn.setMemo(memo);
        txn.setFITxnId(fitxnid);
        txn.setDatePosted(date);
        txn.setDateInitiated(date);
        txn.setDateAvailable(date);
    }
}
