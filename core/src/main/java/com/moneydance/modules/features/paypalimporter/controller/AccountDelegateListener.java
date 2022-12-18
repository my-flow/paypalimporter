package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.AccountListener;

/**
 * Listener / observer on Moneydance's list of accounts.
 *
 * @author Florian J. Breunig
 */
final class AccountDelegateListener implements AccountListener {

    private final ViewController viewController;

    AccountDelegateListener(final ViewController argViewController) {
        this.viewController = argViewController;
    }

    @Override
    public void accountModified(final Account account) {
        this.viewController.refreshAccounts(null);
    }

    @Override
    public void accountBalanceChanged(final Account account) {
        // ignore
    }

    @Override
    public void accountDeleted(
            final Account parentAccount,
            final Account account) {
        this.viewController.refreshAccounts(null);
    }

    @Override
    public void accountAdded(
            final Account parentAccount,
            final Account account) {
        this.viewController.refreshAccounts(null);
    }
}
