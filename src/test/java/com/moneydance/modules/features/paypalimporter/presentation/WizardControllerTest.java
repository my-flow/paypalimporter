// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.presentation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.DateRange;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.controller.StubMoneydanceGUIFactory;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class WizardControllerTest {

    private WizardController wizardController;

    @Before
    public void setUp() {
        StubMoneydanceGUIFactory factory = new StubMoneydanceGUIFactory(new StubContextFactory());
        this.wizardController = new WizardController(null, factory.getMoneydanceGUI());
    }

    @Test
    public void testSetInputData() {
        this.wizardController.setInputData(new InputData(null, null, null, -1));
        assertThat(this.wizardController.txtUsername.getText(), is(""));
        assertThat(String.valueOf(this.wizardController.txtPassword.getPassword()), is(""));
        assertThat(this.wizardController.txtSignature.getText(), is(""));
    }

    @Test
    public void testSetInputDataFocusOnUsername() {
        InputData inputData = new InputData(null, null, "mock signature", -1, new DateRange());
        this.wizardController.setInputData(inputData);
        this.wizardController.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(this.wizardController.txtUsername.isFocusOwner(), is(true));
    }

    @Test
    public void testSetInputDataFocusOnPassword() {
        this.wizardController.setInputData(
                new InputData("mock username", null, "mock signature", -1));
        this.wizardController.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(this.wizardController.txtPassword.isFocusOwner(), is(true));
    }

    @Test
    public void testSetInputDataFocusOnSignature() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.wizardController.setInputData(
                new InputData("mock username", password, null, -1));
        this.wizardController.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(this.wizardController.txtSignature.isFocusOwner(), is(true));
    }

    @Test
    public void testSetBoundedRangeModel() {
        final BoundedRangeModel model = new DefaultBoundedRangeModel();
        this.wizardController.setBoundedRangeModel(model);
    }

    @Test
    public void testSetAccounts() {
        this.wizardController.setAccounts(new DefaultComboBoxModel());
    }

    @Test
    public void testRefresh() {
        this.wizardController.refresh(false, false);
        this.wizardController.refresh(false, true);
        this.wizardController.refresh(true,  false);
        this.wizardController.refresh(true,  true);
    }

    @Test
    public void testUpdateValidation() {
        for (InputDataValidator.MessageKey messageKey : InputDataValidator.MessageKey.values()) {
            this.wizardController.updateValidation("stub text", messageKey);
        }
    }

    @Test
    public void testActionPerformed() {
        this.wizardController.actionPerformed(null);
    }

    @Test
    public void testWindowOpened() {
        this.wizardController.windowOpened(null);
    }

    @Test
    public void testWindowClosing() {
        this.wizardController.windowClosing(null);
    }

    @Test
    public void testWindowClosed() {
        this.wizardController.windowClosed(null);
    }

    @Test
    public void testWindowIconified() {
        this.wizardController.windowIconified(null);
    }

    @Test
    public void testWindowDeiconified() {
        this.wizardController.windowDeiconified(null);
    }

    @Test
    public void testWindowActivated() {
        this.wizardController.windowActivated(null);
    }

    @Test
    public void testWindowDeactivated() {
        this.wizardController.windowDeactivated(null);
    }

}
