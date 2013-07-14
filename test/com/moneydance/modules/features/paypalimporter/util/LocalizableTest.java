// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;

/**
 * @author Florian J. Breunig
 */
public final class LocalizableTest {

    private Localizable localizable;

    @Before
    public void setUp() {
        new StubContextFactory();
        this.localizable = Localizable.INSTANCE;
        this.localizable.update();
    }

    @Test
    public void testUpdate() {
        this.localizable.update();
    }

    @Test
    public void testGetResourceBundle() {
        assertThat(this.localizable.getResourceBundle(), notNullValue());
    }

    @Test
    public void testGetLabelButtonText() {
        assertThat(this.localizable.getLabelButtonText(), notNullValue());
    }

    @Test
    public void testGetNameNewAccount() {
        assertThat(this.localizable.getNameNewAccount(), notNullValue());
    }

    @Test
    public void testGetUrlNewAccount() {
        assertThat(this.localizable.getUrlNewAccount(), notNullValue());
    }

    @Test
    public void testGetUrlHelp() {
        assertThat(this.localizable.getUrlHelp(), notNullValue());
    }

    @Test
    public void testGetErrorMessageUsernameBlank() {
        assertThat(this.localizable.getErrorMessageUsernameBlank(), notNullValue());
    }

    @Test
    public void testGetErrorMessagePasswordBlank() {
        assertThat(this.localizable.getErrorMessagePasswordBlank(), notNullValue());
    }

    @Test
    public void testGetErrorMessageSignatureBlank() {
        assertThat(this.localizable.getErrorMessageSignatureBlank(), notNullValue());
    }

    @Test
    public void testGetErrorMessageStartDateNotBeforeEndDate() {
        assertThat(this.localizable.getErrorMessageStartDateNotBeforeEndDate(), notNullValue());
    }

    @Test
    public void testGetErrorMessageServiceCallFailed() {
        assertThat(
                this.localizable.getErrorMessageServiceCallFailed("stub error message"),
                notNullValue());
    }

    @Test
    public void testGetErrorMessageConnectionFailed() {
        assertThat(this.localizable.getErrorMessageConnectionFailed(), notNullValue());
    }
}
