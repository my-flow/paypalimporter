// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubAccountBookFactory;
import com.moneydance.apps.md.controller.StubContextFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class PreferencesTest {

    private Preferences prefs;
    private StubContextFactory factory;

    @Before
    public void setUp() {
        this.factory = new StubContextFactory();
        this.prefs = new Preferences(
                factory.getContext(),
                new StubAccountBookFactory(
                        this.factory.getContext().getAccountBook()));
    }

    @Test(expected = AssertionError.class)
    public void testGetUserPreferencesWithoutAccountBook() {
        Preferences preferences = new Preferences(
                this.factory.getContext(),
                new StubAccountBookFactory(null));
        preferences.setAllWritablePreferencesToNull();
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
        this.prefs.assignBankingFI(this.factory.getContext().getRootAccount()
                .getSubAccount(0).getAccountNum());
    }

    @Test
    public void testSetUsername() {
        final String username = "stub username";
        this.prefs.setUsername(0, username);
        assertThat(this.prefs.getUsername(-1), equalTo(username));
    }

    @Test
    public void testGetUsername() {
        this.prefs.getUsername(-1);
    }

    @Test
    public void testSetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.prefs.setPassword(0, password);
        assertThat(String.valueOf(this.prefs.getPassword(0)),
                equalTo(String.valueOf(password)));
    }

    @Test
    public void testGetPassword() {
        this.prefs.getPassword(0);
    }

    @Test
    public void testSetSignature() {
        final String signature = "stub signature";
        this.prefs.setSignature(0, signature);
        assertThat(this.prefs.getSignature(0), equalTo(signature));
    }

    @Test
    public void testGetSignature() {
        this.prefs.getSignature(0);
    }

    @Test
    public void testSetAccountId() {
        final int accountId = 1;
        this.prefs.setAccountId(accountId);
        assertThat(this.prefs.getAccountId(), equalTo(accountId));
    }

    @Test
    public void testGetAccountId() {
        this.prefs.getAccountId();
    }

    @Test
    public void testHasUsedCombination() {
        final String username = "stub username";
        assertThat(this.prefs.hasUsedCombination(0, username), equalTo(false));
    }
}
