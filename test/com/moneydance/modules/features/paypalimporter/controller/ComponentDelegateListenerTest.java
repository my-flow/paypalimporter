/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.controller;

import java.awt.event.ComponentAdapter;

import org.junit.Before;
import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.controller.ComponentDelegateListener;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;

/**
 * @author Florian J. Breunig
 */
public final class ComponentDelegateListenerTest {

    private ComponentAdapter componentAdapter;

    @Before
    public void setUp() throws Exception {
        StubContextFactory factory = new StubContextFactory();
        ViewController viewController = new ViewControllerMock();

        this.componentAdapter = new ComponentDelegateListener(
                factory.getContext(),
                viewController);
    }

    @Test
    public void testComponentShownComponentEvent() {
        this.componentAdapter.componentShown(null);
    }

    @Test
    public void testComponentHiddenComponentEvent() {
        this.componentAdapter.componentHidden(null);
    }
}
