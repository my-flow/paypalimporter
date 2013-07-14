// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.util.Helper;

/**
 * @author Florian J. Breunig
 */
public final class ViewControllerImplTest {

    private ViewController viewController;

    @Before
    public void setUp() {
        this.viewController = new ViewControllerImpl(
                new StubContextFactory().getContext(),
                Helper.INSTANCE.getTracker(0));
        this.viewController.startWizard();
    }

    @Test
    public void testStartWizard() {
        this.viewController.startWizard();
    }

    @Test
    public void testUpdate() {
        for (WizardHandler.ExecutedAction action : WizardHandler.ExecutedAction.values()) {
            this.viewController.update(new Observable(), action);
        }
    }

    @Test
    public void testCancel() {
        this.viewController.cancel();
    }

    @Test
    public void testUnlock() {
        this.viewController.unlock("stub text", "stub key");
    }

    @Test
    public void testTransactionsImported() {
        this.viewController.transactionsImported(new StubContextFactory()
        .getContext().getRootAccount().getSubAccount(0));
    }
}
