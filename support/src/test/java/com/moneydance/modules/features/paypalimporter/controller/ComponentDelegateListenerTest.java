// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.StubContextFactory;

import java.awt.event.ComponentAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class ComponentDelegateListenerTest {

    private ComponentAdapter componentAdapter;

    @Before
    public void setUp() {
        StubContextFactory factory = new StubContextFactory();
        ViewController viewController = new ViewControllerMock();

        this.componentAdapter = new ComponentDelegateListener(
                factory.getContext().getAccountBook(),
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
