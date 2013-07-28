// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class MainTest {

    private Main main;

    @Before
    public void setUp() {
        this.main = new Main();
        final StubContextFactory factory = new StubContextFactory(this.main);
        factory.init();
        this.main.init();
    }

    @Test
    public void testInit() {
        this.main.init();
    }

    @Test
    public void testUnload() {
        this.main.unload();
    }

    @Test
    public void testCleanup() {
        this.main.cleanup();
    }

    @Test
    public void testGetName() {
        assertThat(this.main.getName(), notNullValue());
    }

    @Test
    public void testGetIconImage() {
        assertThat(this.main.getIconImage(), notNullValue());
    }

    @Test
    public void testInvokeString() {
        this.main.invoke("");
        this.main.invoke(Helper.INSTANCE.getSettings().getStartWizardSuffix());
    }

    @Test
    public void testUpdate() {
        this.main.update(null, null);
    }
}
