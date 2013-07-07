/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.presentation;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.validation.view.ValidationComponentUtils;
import com.moneydance.apps.md.view.gui.DateRangeChooser;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;
import com.moneydance.modules.features.paypalimporter.util.Helper;

/**
 * @author Florian J. Breunig
 */
class WizardController extends WizardLayout
implements ActionListener, WindowListener {

    private static final long serialVersionUID = 1L;

    WizardController(final Frame owner, final MoneydanceGUI mdGUI) {
        super(owner, mdGUI,
                Helper.INSTANCE.getLocalizable().getResourceBundle());

        // preset ESC close operation
        Helper.INSTANCE.installEscapeCloseOperation(this);

        // preset button groupings
        final ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(this.rdBtnExistingAcct);
        accountGroup.add(this.rdBtnNewAcct);

        // preset mandatory fields
        ValidationComponentUtils.setMandatory(this.txtUsername, true);
        ValidationComponentUtils.setMandatory(this.txtPassword, true);
        ValidationComponentUtils.setMandatory(this.txtSignature, true);

        // preset selections
        this.dateRanger.setOption(DateRangeChooser.DR_THIS_QUARTER);
        this.progressBar.setIndeterminate(true);
        this.getRootPane().setDefaultButton(this.btnProceed);

        final ActionListener refreshListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                WizardController.this.refresh(false, null);
            }
        };
        final PropertyChangeListener propertyChangeListener =
                new PropertyChangeListener() {
            @Override
            public void propertyChange(
                    final PropertyChangeEvent propertychangeevent) {
                refreshListener.actionPerformed(null);
            }
        };

        this.rdBtnExistingAcct.addActionListener(refreshListener);
        this.rdBtnNewAcct.addActionListener(refreshListener);
        this.dateRanger.addPropertyChangeListener(propertyChangeListener);

        this.btnProceed.addActionListener(this);
        this.btnCancel.addActionListener(this);
        this.btnHelp.addActionListener(this);
        this.addWindowListener(this);

        this.refresh(true, null);
    }

    public final void setInputData(final InputData inputData) {
        Validate.notNull(inputData, "input data must not be null");

        this.txtUsername.setText(inputData.getUsername());
        if (inputData.getPassword(false) != null) {
            this.txtPassword.setText(
                    String.valueOf(inputData.getPassword(true)));
        }
        this.txtSignature.setText(inputData.getSignature());
        if (inputData.getDateRange() != null) {
            this.dateRanger.setStartDate(
                    inputData.getDateRange().getStartDateInt());
            this.dateRanger.setEndDate(
                    inputData.getDateRange().getEndDateInt());
        }

        // preset focus
        if (StringUtils.isEmpty(this.txtUsername.getText())) {
            this.txtUsername.requestFocus();
        } else if (ArrayUtils.isEmpty(this.txtPassword.getPassword())) {
            this.txtPassword.requestFocus();
        } else if (StringUtils.isEmpty(this.txtSignature.getText())) {
            this.txtSignature.requestFocus();
        }
    }

    public final void setAccounts(final ComboBoxModel accountModel) {
        Validate.notNull(accountModel, "account model must not be null");
        this.comboBoxAccts.setModel(accountModel);

        this.refresh(true, null);
    }

    public final void refresh(final boolean initialize, final Boolean loading) {
        final boolean isLoading;
        if (loading == null) {
            isLoading = !this.txtUsername.isEnabled();
        } else {
            isLoading = loading.booleanValue();
        }
        final ComboBoxModel accountModel = this.comboBoxAccts.getModel();
        final boolean acctsExist = accountModel.getSize() > 0;

        this.rdBtnExistingAcct.setEnabled(!isLoading && acctsExist);
        if (initialize) {
            this.rdBtnNewAcct.setSelected(true);

            if (acctsExist) {
                // make sure there is always a selection
                if (accountModel.getSelectedItem() == null) {
                    this.comboBoxAccts.setSelectedIndex(0);
                } else {
                    this.comboBoxAccts.setSelectedItem(
                            accountModel.getSelectedItem());
                    this.rdBtnExistingAcct.setSelected(true);
                }
            }
        }

        boolean newAcctEnabled = this.rdBtnNewAcct.isSelected();
        this.comboBoxAccts.setEnabled(!isLoading && !newAcctEnabled);

        this.comboBoxDateRange.setEnabled(!isLoading);
        this.dateRanger.getStartField().setEnabled(!isLoading);
        this.dateRanger.getEndField().setEnabled(!isLoading);

        this.txtUsername.setEnabled(!isLoading);
        this.btnHelp.setEnabled(!isLoading);
        this.txtPassword.setEnabled(!isLoading);
        this.txtSignature.setEnabled(!isLoading);
        this.rdBtnNewAcct.setEnabled(!isLoading);
        this.btnProceed.setEnabled(!isLoading);

        this.progressBar.setVisible(isLoading);
    }

    /**
     * @param text error message to be displayed (can be null)
     * @param key identifier of the related input field (can be null)
     */
    public final void updateValidation(final String text, final Object key) {
        this.refresh(false, Boolean.FALSE);

        if (InputDataValidator.MessageKey.USERNAME.equals(key)) {
            this.txtUsername.requestFocus();
        }

        if (InputDataValidator.MessageKey.PASSWORD.equals(key)) {
            this.txtPassword.requestFocus();
        }

        if (InputDataValidator.MessageKey.SIGNATURE.equals(key)) {
            this.txtSignature.requestFocus();
        }

        if (InputDataValidator.MessageKey.DATERANGE.equals(key)) {
            this.dateRanger.getEndField().requestFocus();
        }

        if (text != null) {
            ValidationComponentUtils
            .updateComponentTreeMandatoryAndBlankBackground(
                    this.panelBuilder.getPanel());

            final Component parentComponent;
            if (this.isVisible()) {
                parentComponent = this;
            } else {
                parentComponent = null;
            }
            final Object errorLabel = new JLabel(text);
            JOptionPane.showMessageDialog(
                    parentComponent,
                    errorLabel,
                    null, // no title
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        // hook
    }

    @Override
    public void windowOpened(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowClosing(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowClosed(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowIconified(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowDeiconified(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowActivated(final WindowEvent windowevent) {
        // hook
    }

    @Override
    public void windowDeactivated(final WindowEvent windowevent) {
        // hook
    }
}
