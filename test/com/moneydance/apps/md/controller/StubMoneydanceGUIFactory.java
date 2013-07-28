// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.moneydance.apps.md.view.gui.MoneydanceGUI;

import org.apache.commons.lang.Validate;

/**
 * @author Florian J. Breunig
 */
public final class StubMoneydanceGUIFactory {

    private final StubContextFactory stubContextFactory;

    public StubMoneydanceGUIFactory(
            final StubContextFactory argStubContextFactory) {
        Validate.notNull(argStubContextFactory,
                "stub context factory must not be null");
        this.stubContextFactory = argStubContextFactory;
    }

    public MoneydanceGUI getMoneydanceGUI() {
        // Using undocumented feature.
        Main main = this.stubContextFactory.getContext();
        if (main != null) {
            return (MoneydanceGUI) main.getUI();
        }
        return null;
    }
}
