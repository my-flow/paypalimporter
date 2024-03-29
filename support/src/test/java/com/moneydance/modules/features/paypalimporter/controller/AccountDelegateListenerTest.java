package com.moneydance.modules.features.paypalimporter.controller;

import org.junit.Before;
import org.junit.Test;

import com.infinitekind.moneydance.model.AccountListener;

public final class AccountDelegateListenerTest {

    private AccountListener accountListener;

    @Before
    public void setUp() {
        ViewController viewController = new ViewControllerMock();
        this.accountListener = new AccountDelegateListener(viewController);
    }

    @Test
    public void testAccountModified() {
        this.accountListener.accountModified(null);
    }

    @Test
    public void testAccountBalanceChanged() {
        this.accountListener.accountBalanceChanged(null);
    }

    @Test
    public void testAccountDeleted() {
        this.accountListener.accountDeleted(null, null);
    }

    @Test
    public void testAccountAdded() {
        this.accountListener.accountAdded(null, null);
    }
}
