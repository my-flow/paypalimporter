// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import java.util.Date;
import java.util.List;
import java.util.Observable;

import com.moneydance.modules.features.paypalimporter.model.InputData;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.OnlineTxn;

import javax.annotation.Nullable;

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
    public void proceed(final InputData inputData) {
        // ignore
    }

    @Override
    public void unlock() {
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
            @Nullable final Account account,
            @Nullable final String errorCode) {
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
