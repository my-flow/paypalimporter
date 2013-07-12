/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.controller;

import java.util.Observable;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;

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
            final boolean isPrimaryCurrency) {
        // ignore
    }

    @Override
    public void transactionsImported(final Account account) {
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
