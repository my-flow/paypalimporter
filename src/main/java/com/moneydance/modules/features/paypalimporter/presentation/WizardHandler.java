// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Observer;

import org.apache.commons.lang3.Validate;

import com.infinitekind.moneydance.model.Account;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.model.InputData;

/**
 * This reactive class forwards UI events to its observers. The observers can
 * identify the events based on the passed type of <code>ExecutedAction</code>
 * (loose coupling).
 *
 * @author Florian J. Breunig
 */
public final class WizardHandler extends WizardController {

    private static final long serialVersionUID = 1L;

    private final Observer observer;
    private InputData inputData;

    /**
     * @author Florian J. Breunig
     */
    public enum ExecutedAction {
        START_WIZARD,
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
        this.inputData = new InputData(null,  null,  null,  -1);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.btnHelp)) {
            this.observer.update(null, ExecutedAction.SHOW_HELP);
        } else if (actionEvent.getSource().equals(this.btnProceed)) {

            int accountId = -1;
            if (this.rdBtnExistingAcct.isSelected()) {
                final Account account = (Account)
                        this.comboBoxAccts.getSelectedItem();
                accountId = account.getAccountNum();
            }

            this.inputData = new InputData(
                    this.txtUsername.getText(),
                    this.txtPassword.getPassword(),
                    this.txtSignature.getText(),
                    accountId,
                    this.dateRanger.getDateRange());

            this.observer.update(null, ExecutedAction.PROCEED);

        } else if (actionEvent.getSource().equals(this.btnCancel)) {
            this.observer.update(null, ExecutedAction.CANCEL);
        }
    }

    /**
     * @return latest user input.
     */
    public InputData getInputData() {
        return this.inputData;
    }

    @Override
    public void windowClosing(final WindowEvent event) {
        if (!this.progressBar.isVisible()) {
            this.observer.update(null, ExecutedAction.CANCEL);
        }
    }
}
