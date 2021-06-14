// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class PayPalOnlineServiceTest {

    private static final String USER_NAME = "mock username";
    private static final String SIGNATURE = "mock signature";
    private static final char[] PASSWORD = {'s', 't', 'u', 'b', ' ', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    private static final String ACCOUNT_ID = UUID.randomUUID().toString();

    private PayPalOnlineService service;
    private IAccountBook accountBook;

    @Before
    public void setUp() {
        this.accountBook = new StubContextFactory().getContext().getAccountBook();
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.service = new OnlineServiceFactory(supportComponent.settings()).createService(this.accountBook);
    }

    @Test
    public void testAssignToAccount() {
        this.service.assignToAccount(
                this.accountBook,
                this.accountBook.getRootAccount().getSubAccount(0).getUUID());
        // assign twice
        this.service.assignToAccount(
                this.accountBook,
                this.accountBook.getRootAccount().getSubAccount(0).getUUID());
    }

    @Test
    public void testSetUsername() {
        this.service.setUsername(ACCOUNT_ID, USER_NAME);
    }

    @Test
    public void testGetUsername() {
        this.service.setUsername(ACCOUNT_ID, USER_NAME);
        assertThat(this.service.getUsername(ACCOUNT_ID), is(USER_NAME));
    }

    @Test
    public void testSetPassword() {
        this.service.setPassword(ACCOUNT_ID, PASSWORD);
    }

    @Test
    public void testGetPassword() {
        this.service.setPassword(ACCOUNT_ID, PASSWORD);
        assertThat(String.valueOf(this.service.getPassword(ACCOUNT_ID)), is(String.valueOf(PASSWORD)));
    }

    @Test
    public void testSetSignature() {
        this.service.setSignature(ACCOUNT_ID, SIGNATURE);
    }

    @Test
    public void testGetSignature() {
        this.service.setSignature(ACCOUNT_ID, SIGNATURE);
        assertThat(this.service.getSignature(ACCOUNT_ID), is(SIGNATURE));
    }

    @Test
    public void testSetAccountId() {
        this.service.setAccountId(UUID.randomUUID().toString());
    }

    @Test
    public void testGetAccountId() {
        this.service.setAccountId(ACCOUNT_ID);
        assertThat(this.service.getAccountId(), is(ACCOUNT_ID));
    }

    @Test
    public void testBuildRealm() {
        assertThat(PayPalOnlineService.buildRealm(null), is(OnlineService.DEFAULT_REQ_REALM));
        assertThat(PayPalOnlineService.buildRealm("-1"), is(OnlineService.DEFAULT_REQ_REALM));
        assertThat(PayPalOnlineService.buildRealm(""), not(OnlineService.DEFAULT_REQ_REALM));
        assertThat(PayPalOnlineService.buildRealm("123-123"), not(OnlineService.DEFAULT_REQ_REALM));
        assertThat(PayPalOnlineService.buildRealm("1"), not(OnlineService.DEFAULT_REQ_REALM));
    }
}
