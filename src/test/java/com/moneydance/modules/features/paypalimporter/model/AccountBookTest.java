// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.apps.md.controller.StubContextFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class AccountBookTest {

    private IAccountBook accountBook;

    @Before
    public void setUp() throws Exception {
        StubContextFactory factory = new StubContextFactory();
        this.accountBook = AccountBookFactoryImpl.INSTANCE.createAccountBook(
                factory.getContext());
    }

    @Test
    public void testGetOnlineInfo() {
        assertThat(this.accountBook.getOnlineInfo(), notNullValue());
    }

    @Test
    public void testGetAccountByNum() {
        assertThat(this.accountBook.getAccountByNum(0), nullValue());
    }

    @Test
    public void testGetWrappedOriginal() {
        assertThat(this.accountBook.getWrappedOriginal(), notNullValue());
    }

    @Test
    public void testGetCurrencies() {
        assertThat(this.accountBook.getCurrencies(), notNullValue());
    }

    @Test
    public void testGetRootAccount() {
        assertThat(this.accountBook.getRootAccount(), notNullValue());
    }

    @Test
    public void testLogRemovedItem() {
        OnlineService onlineService = new OnlineService(
                this.accountBook.getWrappedOriginal());
        assertThat(this.accountBook.logRemovedItem(onlineService),
                equalTo(false));
    }

    @Test
    public void testAddAccountListener() {
        this.accountBook.addAccountListener(null);
    }

    @Test
    public void testRemoveAccountListener() {
        this.accountBook.removeAccountListener(null);
    }

}
