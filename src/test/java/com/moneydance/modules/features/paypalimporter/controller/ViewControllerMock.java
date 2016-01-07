// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import java.util.Date;
import java.util.List;
import java.util.Observable;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.OnlineTxn;

/**
 * @author Florian J. Breunig
 */
final class ViewControllerMock implements ViewController {

    @Override
    public void update(final Observable observable, final Object obj) {
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
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode,
            final List<CurrencyCodeType> currencyCodes) {
        // ignore
    }

    @Override
    public void transactionsImported(
            final List<OnlineTxn> onlineTxns,
            final Date argStartDate,
            final Account account,
            final String errorCode) {
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
