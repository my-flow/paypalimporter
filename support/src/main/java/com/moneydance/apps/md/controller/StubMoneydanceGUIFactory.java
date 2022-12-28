package com.moneydance.apps.md.controller;

import com.moneydance.apps.md.view.gui.MoneydanceGUI;

import javax.annotation.Nullable;

public final class StubMoneydanceGUIFactory {

    private final StubContextFactory stubContextFactory;

    public StubMoneydanceGUIFactory(
            final StubContextFactory argStubContextFactory) {
        this.stubContextFactory = argStubContextFactory;
    }

    @Nullable public MoneydanceGUI getMoneydanceGUI() {
        // Using undocumented feature.
        Main main = this.stubContextFactory.getContext();
        if (main != null) {
            return (MoneydanceGUI) main.getUI();
        }
        return null;
    }
}
