// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.presentation;

import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.MutableInputData;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Observer;

import org.apache.commons.lang3.Validate;

/**
 * @author Florian J. Breunig
 */
public final class WizardHandler extends WizardController {

    private static final long serialVersionUID = 1L;

    private final Observer observer;
    private final MutableInputData mutableInputData;

    /**
     * @author Florian J. Breunig
     */
    public enum ExecutedAction {
        SHOW_HELP,
        CANCEL,
        PROCEED;
    }

    public WizardHandler(
            final Frame owner,
            final MoneydanceGUI mdGUI,
            final Observer argObserver) {
        super(owner, mdGUI);
        Validate.notNull(mdGUI, "Moneydance GUI must not be null");
        Validate.notNull(argObserver, "observer must not be null");
        this.observer = argObserver;
        this.mutableInputData = new MutableInputData();
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.btnHelp)) {
            this.observer.update(null, ExecutedAction.SHOW_HELP);
        } else if (actionEvent.getSource().equals(this.btnProceed)) {

            int accountNum = -1;
            if (this.rdBtnExistingAcct.isSelected()) {
                final Account account = (Account)
                        this.comboBoxAccts.getSelectedItem();
                accountNum = account.getAccountNum();
            }

            this.mutableInputData.fill(
                    this.txtUsername.getText(),
                    this.txtPassword.getPassword(),
                    this.txtSignature.getText(),
                    accountNum,
                    this.dateRanger.getDateRange());

            this.observer.update(null, ExecutedAction.PROCEED);

        } else if (actionEvent.getSource().equals(this.btnCancel)) {
            this.observer.update(null, ExecutedAction.CANCEL);
        }
    }

    public InputData getInputData() {
        return this.mutableInputData;
    }

    @Override
    public void windowClosing(final WindowEvent event) {
        if (!this.progressBar.isVisible()) {
            this.observer.update(null, ExecutedAction.CANCEL);
        }
    }
}
