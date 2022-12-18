package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.apps.md.controller.StubContextFactory;

import org.junit.Before;
import org.junit.Test;

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
    public void testCreateAccount() {
        Account account = this.accountBook.createBankAccount(
                "new account name",
                null,
                "account URL");
        assertThat(account, notNullValue());
        assertThat(this.accountBook.getAccountById(account.getUUID()), is(account));
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
