// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class HelperTest {

    @Test
    public void testGetPreferences() {
        assertThat(Helper.INSTANCE.getPreferences(), notNullValue());
    }

    @Test
    public void testGetLocalizable() {
        assertThat(Helper.INSTANCE.getLocalizable(), notNullValue());
    }

    @Test
    public void testGetTracker() {
        assertThat(Helper.INSTANCE.getTracker(0), notNullValue());
    }

    @Test
    public void testGetInputStreamFromResource() {
        assertThat(Helper.getInputStreamFromResource(
                Helper.INSTANCE.getSettings().getLoggingPropertiesResource()),
                notNullValue());
    }
}