package com.moneydance.modules.features.paypalimporter.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Observer;

import com.infinitekind.moneydance.model.Account;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.annotation.Nullable;

/**
 * This reactive class forwards UI events to its observers. The observers can
 * identify the events based on the passed type of <code>ExecutedAction</code>
 * (loose coupling).
 */
public final class WizardHandler extends WizardController {

    private static final long serialVersionUID = 1L;

    private final Observer observer;
    private InputData inputData;

    public enum ExecutedAction {
        START_WIZARD,
        SHOW_HELP,
        CANCEL,
        PROCEED;
    }

    @SuppressWarnings("nullness")
    public WizardHandler(
            @Nullable final Frame owner,
            final MoneydanceGUI mdGUI,
            final Observer argObserver,
            final Localizable localizable,
            final Settings settings) {
        super(owner,
                mdGUI,
                localizable.getResourceBundle(),
                settings);
        this.observer = argObserver;
        this.inputData = new InputData();
    }

    @Override
    @SuppressWarnings("nullness")
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.btnHelp)) {
            this.observer.update(null, ExecutedAction.SHOW_HELP);
        } else if (actionEvent.getSource().equals(this.btnProceed)) {

            String accountId = null;
            if (this.rdBtnExistingAcct.isSelected()) {
                final Account account = (Account)
                        this.comboBoxAccts.getSelectedItem();
                accountId = account.getUUID();
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
    @SuppressWarnings("nullness")
    public void windowClosing(final WindowEvent event) {
        if (!this.progressBar.isVisible()) {
            this.observer.update(null, ExecutedAction.CANCEL);
        }
    }
}
