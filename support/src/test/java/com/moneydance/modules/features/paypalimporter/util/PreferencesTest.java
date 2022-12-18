package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.Account;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class PreferencesTest {

    private static final String ACCOUNT_ID = UUID.randomUUID().toString();

    private Preferences prefs;
    private StubAccountBook accountBook;
    private Account account;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.prefs = supportComponent.preferences();

        this.account = supportComponent.accountBook().getRootAccount();
        this.accountBook = (StubAccountBook) supportComponent.accountBook();
    }

    @Test
    public void testGetUserPreferences() {
        assertThat(this.prefs.getLocale(), notNullValue());
    }

    @Test
    public void testSetAllWritablePreferencesToNull() {
        this.prefs.setAllWritablePreferencesToNull();
    }

    @Test
    public void testSetFirstRun() {
        final boolean firstRun = false;
        this.prefs.setFirstRun(firstRun);
        assertThat(this.prefs.isFirstRun(), equalTo(firstRun));
    }

    @Test
    public void testIsFirstRun() {
        this.prefs.isFirstRun();
    }

    @Test
    public void testGetLocale() {
        assertThat(this.prefs.getLocale(), notNullValue());
    }

    @Test
    public void testHasProxy() {
        this.prefs.hasProxy();
    }

    @Test
    public void testGetProxyHost() {
        assertThat(this.prefs.getProxyHost(), anything());
    }

    @Test
    public void testGetProxyPort() {
        assertThat(this.prefs.getProxyPort(), anything());
    }

    @Test
    public void testHasProxyAuthentication() {
        assertThat(this.prefs.hasProxyAuthentication(), anything());
    }

    @Test
    public void testGetProxyUsername() {
        assertThat(this.prefs.getProxyUsername(), anything());
    }

    @Test
    public void testGetProxyPassword() {
        assertThat(this.prefs.getProxyPassword(), anything());
    }

    @Test
    public void testSetBankingFI() {
        this.accountBook.addAccount(this.account);
        this.prefs.assignBankingFI(this.account.getUUID());
        this.accountBook.removeAccount(this.account);
    }

    @Test
    public void testSetUsername() {
        final String username = "stub username";
        this.prefs.setUsername(ACCOUNT_ID, username);
        assertThat(this.prefs.getUsername(ACCOUNT_ID), equalTo(username));
    }

    @Test
    public void testGetUsername() {
        this.prefs.getUsername(null);
    }

    @Test
    public void testSetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.prefs.setPassword(ACCOUNT_ID, password);
        assertThat(String.valueOf(this.prefs.getPassword(ACCOUNT_ID)),
                equalTo(String.valueOf(password)));
    }

    @Test
    public void testGetPassword() {
        this.prefs.getPassword(ACCOUNT_ID);
    }

    @Test
    public void testSetSignature() {
        final String signature = "stub signature";
        this.prefs.setSignature(ACCOUNT_ID, signature);
        assertThat(this.prefs.getSignature(ACCOUNT_ID), equalTo(signature));
    }

    @Test
    public void testGetSignature() {
        this.prefs.getSignature(ACCOUNT_ID);
    }

    @Test
    public void testSetAccountId() {
        this.prefs.setAccountId(ACCOUNT_ID);
        assertThat(this.prefs.getAccountId(), equalTo(ACCOUNT_ID));
    }

    @Test
    public void testGetAccountId() {
        this.prefs.getAccountId();
    }

    @Test
    public void testHasUsedCombination() {
        final String username = "stub username";
        assertThat(this.prefs.hasUsedCombination(ACCOUNT_ID, username), equalTo(false));
    }
}
