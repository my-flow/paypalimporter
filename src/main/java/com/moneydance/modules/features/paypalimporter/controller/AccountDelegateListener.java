// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.AccountListener;

import org.apache.commons.lang3.Validate;

/**
 * Listener / observer on Moneydance's list of accounts.
 *
 * @author Florian J. Breunig
 */
final class AccountDelegateListener implements AccountListener {

    private final ViewController viewController;

    AccountDelegateListener(final ViewController argViewController) {
        Validate.notNull(argViewController, "view controller must not be null");
        this.viewController = argViewController;
    }

    @Override
    public void accountModified(final Account account) {
        this.viewController.refreshAccounts(-1);
    }

    @Override
    public void accountBalanceChanged(final Account account) {
        // ignore
    }

    @Override
    public void accountDeleted(
            final Account parentAccount,
            final Account account) {
        this.viewController.refreshAccounts(-1);
    }

    @Override
    public void accountAdded(
            final Account parentAccount,
            final Account account) {
        this.viewController.refreshAccounts(-1);
    }
}
