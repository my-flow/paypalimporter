/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;

/**
 * @author Florian J. Breunig
 */
public final class PreferencesTest {

    private Preferences prefs;
    private StubContextFactory factory;

    @Before
    public void setUp() {
        this.prefs = new Preferences();
        this.factory = new StubContextFactory();
        this.prefs.setContext(this.factory.getContext());
    }

    @Test
    public void testGetUserPreferences() {
        this.prefs = new Preferences();
        Helper.INSTANCE.addObserver(new Observer() {
            @Override
            public void update(final Observable observable,
                    final Object updateAll) {
                PreferencesTest.this.prefs.setContext(PreferencesTest.this.factory.getContext());
            }
        });
        this.prefs.getLocale();

        this.prefs = new Preferences();
        this.prefs.getLocale();
    }

    @Test
    public void testSetAllWritablePreferencesToNull() {
        this.prefs.setAllWritablePreferencesToNull();
    }

    @Test
    public void testSetFirstRun() {
        final boolean firstRun = false;
        this.prefs.setFirstRun(firstRun);
        assertThat(this.prefs.isFirstRun(), is(firstRun));
    }

    @Test
    public void testIsFirstRun() {
        this.prefs.isFirstRun();
    }

    @Test
    public void testGetFullVersion() {
        assertThat(this.prefs.getFullVersion(), notNullValue());
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
        assertThat(this.prefs.getProxyHost(), notNullValue());
    }

    @Test
    public void testGetProxyPort() {
        this.prefs.getProxyPort();
    }

    @Test
    public void testHasProxyAuthentication() {
        this.prefs.hasProxyAuthentication();
    }

    @Test
    public void testGetProxyUsername() {
        assertThat(this.prefs.getProxyUsername(), notNullValue());
    }

    @Test
    public void testGetProxyPassword() {
        assertThat(this.prefs.getProxyPassword(), notNullValue());
    }

    @Test
    public void testSetBankingFI() {
        this.prefs.assignBankingFI(this.factory.getContext().getRootAccount()
                .getSubAccount(0).getAccountNum());
    }

    @Test
    public void testSetUsername() {
        final String username = "stub username";
        this.prefs.setUsername(username);
        assertThat(this.prefs.getUsername(), is(username));
    }

    @Test
    public void testGetUsername() {
        this.prefs.getUsername();
    }

    @Test
    public void testSetPassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.prefs.setPassword(password);
        assertThat(String.valueOf(this.prefs.getPassword()), is(String.valueOf(password)));
    }

    @Test
    public void testGetPassword() {
        this.prefs.getPassword();
    }

    @Test
    public void testSetSignature() {
        final String signature = "stub signature";
        this.prefs.setSignature(signature);
        assertThat(this.prefs.getSignature(), is(signature));
    }

    @Test
    public void testGetSignature() {
        this.prefs.getSignature();
    }

    @Test
    public void testSetAccountId() {
        final int accountId = 0;
        this.prefs.setAccountId(accountId);
        assertThat(this.prefs.getAccountId(), is(accountId));
    }

    @Test
    public void testGetAccountId() {
        this.prefs.getAccountId();
    }
}
