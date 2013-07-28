// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.model.RootAccount;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineServiceTest {

    private PayPalOnlineService service;
    private RootAccount rootAccount;

    @Before
    public void setUp() {
        this.rootAccount = new StubContextFactory().getContext().getRootAccount();
        this.service = OnlineServiceFactory.getService(this.rootAccount);
    }

    @Test
    public void testAssignToAccount() {
        this.service.assignToAccount(
                this.rootAccount,
                this.rootAccount.getSubAccount(0).getAccountNum());
        // assign twice
        this.service.assignToAccount(
                this.rootAccount,
                this.rootAccount.getSubAccount(0).getAccountNum());
        // assign another
        this.service.assignToAccount(
                this.rootAccount,
                this.rootAccount.getSubAccount(1).getAccountNum());
    }

    @Test
    public void testSetUsername() {
        this.service.setUsername("mock username");
    }

    @Test
    public void testGetUsername() {
        final String username = "mock username";
        this.service.setUsername(username);
        assertThat(this.service.getUsername(), is(username));
    }

    @Test
    public void testSetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.service.setPassword(this.rootAccount, password);
    }

    @Test
    public void testGetPasswordStorePin() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.rootAccount.setParameter(RootAccount.STORE_PINS_PARAM, true);
        this.service.setPassword(this.rootAccount, password);
        assertThat(String.valueOf(this.service.getPassword()), is(String.valueOf(password)));
    }

    @Test
    public void testGetPasswordDoNotStorePin() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.rootAccount.setParameter(RootAccount.STORE_PINS_PARAM, false);
        this.service.setPassword(this.rootAccount, password);
        assertThat(this.service.getPassword(), nullValue());
    }

    @Test
    public void testSetSignature() {
        this.service.setSignature("mock signature");
    }

    @Test
    public void testGetSignature() {
        final String signature = "mock signature";
        this.service.setSignature(signature);
        assertThat(this.service.getSignature(), is(signature));
    }

    @Test
    public void testSetAccountId() {
        this.service.setAccountId(0);
    }

    @Test
    public void testGetAccountId() {
        final int accountId = 17;
        this.service.setAccountId(accountId);
        assertThat(this.service.getAccountId(), is(accountId));
    }
}
