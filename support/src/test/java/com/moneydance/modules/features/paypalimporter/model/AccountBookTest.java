// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

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
    public void setUp() {
        StubContextFactory factory = new StubContextFactory();
        this.accountBook = AccountBookFactoryImpl.INSTANCE.createAccountBook(
                factory.getContext()).orElseThrow(AssertionError::new);
    }

    @Test
    public void testGetOnlineInfo() {
        assertThat(this.accountBook.getOnlineInfo(), notNullValue());
    }

    @Test
    public void testGetAccountById() {
        assertThat(this.accountBook.getAccountById("123-123"), nullValue());
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
