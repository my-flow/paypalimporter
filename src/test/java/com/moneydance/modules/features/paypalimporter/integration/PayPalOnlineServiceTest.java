// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineServiceTest {

    private PayPalOnlineService service;
    private IAccountBook accountBook;

    @Before
    public void setUp() {
        this.accountBook = new StubContextFactory().getContext().getAccountBook();
        this.service = OnlineServiceFactory.createService(this.accountBook);
    }

    @Test
    public void testAssignToAccount() {
        this.service.assignToAccount(
                this.accountBook,
                this.accountBook.getRootAccount().getSubAccount(0).getAccountNum());
        // assign twice
        this.service.assignToAccount(
                this.accountBook,
                this.accountBook.getRootAccount().getSubAccount(0).getAccountNum());
        // assign another
        this.service.assignToAccount(
                this.accountBook,
                this.accountBook.getRootAccount().getSubAccount(1).getAccountNum());
    }

    @Test
    public void testSetUsername() {
        this.service.setUsername(0, "mock username");
    }

    @Test
    public void testGetUsername() {
        final String username = "mock username";
        this.service.setUsername(0, username);
        assertThat(this.service.getUsername(0), is(username));
    }

    @Test
    public void testSetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.service.setPassword(0, password);
    }

    @Test
    public void testGetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.service.setPassword(0, password);
        assertThat(String.valueOf(this.service.getPassword(0)), is(String.valueOf(password)));
    }

    @Test
    public void testSetSignature() {
        this.service.setSignature(0, "mock signature");
    }

    @Test
    public void testGetSignature() {
        final String signature = "mock signature";
        this.service.setSignature(0, signature);
        assertThat(this.service.getSignature(0), is(signature));
    }
}
