// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineTxn;

import java.util.Date;
import java.util.List;
import java.util.Observer;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public interface ViewController extends Observer {

    void startWizard();

    void cancel();

    void proceed();

    /**
     * @param text error message to be displayed (can be null)
     * @param key identifier of the related input field (can be null)
     */
    void unlock(
            final String text,
            final Object key);

    void currencyChecked(
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode,
            final List<CurrencyCodeType> currencyCodes);

    void transactionsImported(
            final List<OnlineTxn> onlineTxns,
            final Date argStartDate,
            final Account account,
            final String errorCode);

    void showHelp();

    void refreshAccounts(
            final int accountId);
}
