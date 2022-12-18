package com.moneydance.modules.features.paypalimporter.presentation;

import com.infinitekind.moneydance.model.Account;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.moneydance.apps.md.view.gui.DateRangeChooser;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;
import com.moneydance.modules.features.paypalimporter.bootstrap.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This controller class adds dynamic behaviour to the static
 * <code>WizardLayout</code> class such as updating and validating.
 */
class WizardController extends WizardLayout
implements ActionListener, WindowListener {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            WizardController.class.getName());

    private static final long serialVersionUID = 1L;

    @SuppressWarnings({"initialization", "deprecation"})
    protected WizardController(
            @Nullable final Frame owner,
            final MoneydanceGUI mdGUI,
            final ResourceBundle resourceBundle,
            final Settings settings) {
        super(owner,
                mdGUI,
                resourceBundle,
                settings);

        // preset ESC close operation
        Helper.installEscapeCloseOperation(this);

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

        // register listeners
        final ActionListener refreshListener = event ->
                this.refresh(false, null);
        this.rdBtnExistingAcct.addActionListener(refreshListener);
        this.rdBtnNewAcct.addActionListener(refreshListener);

        try {
            final PropertyChangeListener propertyChangeListener = propertyChangeEvent ->
                refreshListener.actionPerformed(null);
            this.dateRanger.addPropertyChangeListener(propertyChangeListener);
        } catch (NoSuchMethodError e) {
            // ignore exception in older versions of Moneydance
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.FINE, message, e);
            }
        }

        this.btnProceed.addActionListener(this);
        this.btnCancel.addActionListener(this);
        this.btnHelp.addActionListener(this);
        this.addWindowListener(this);

        this.refresh(true, null);
    }

    /**
     * @return true iff the wizard is blocked due to loading.
     */
    public boolean isLoading() {
        return !this.txtUsername.isEnabled();
    }

    @SuppressWarnings("nullness")
    public final void setInputData(final InputData inputData) {
        inputData.getUsername().ifPresent(this.txtUsername::setText);

        AtomicReference<String> password = new AtomicReference<>(null);
        inputData.getPassword(true).ifPresent(pass -> password.set(String.valueOf(pass)));
        this.txtPassword.setText(password.get());

        inputData.getSignature().ifPresent(this.txtSignature::setText);

        inputData.getDateRange().ifPresent(dateRange -> {
                this.dateRanger.setStartDate(dateRange.getStartDateInt());
                this.dateRanger.setEndDate(dateRange.getEndDateInt());
        });

        // preset focus
        if (StringUtils.isEmpty(this.txtUsername.getText())) {
            this.txtUsername.requestFocusInWindow();
        } else if (ArrayUtils.isEmpty(this.txtPassword.getPassword())) {
            this.txtPassword.requestFocusInWindow();
        } else if (StringUtils.isEmpty(this.txtSignature.getText())) {
            this.txtSignature.requestFocusInWindow();
        }
    }

    /**
     * Update progress.
     *
     * @param boundedRangeModel Data source of the progress bar.
     */
    public final void setBoundedRangeModel(
            final BoundedRangeModel boundedRangeModel) {

        this.progressBar.setModel(boundedRangeModel);
        this.progressBar.setIndeterminate(false);
    }


    /**
     * Update the list of accounts.
     *
     * @param accountModel Data source of the accounts combobox.
     */
    @SuppressWarnings("unchecked")
    public final void setAccounts(final ComboBoxModel accountModel) {
        this.comboBoxAccts.setModel(accountModel);
        this.refresh(true, null);
    }

    public final void refresh(final boolean initialize, @Nullable final Boolean loading) {
        final boolean isLoading;
        if (loading == null) {
            isLoading = this.isLoading();
        } else {
            isLoading = loading;
        }
        final ComboBoxModel<Account> accountModel =
                this.comboBoxAccts.getModel();
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

        this.progressBar.setIndeterminate(true);
        this.progressBar.setVisible(isLoading);
    }

    /**
     * @param text error message to be displayed
     * @param key identifier of the related input field
     */
    @SuppressWarnings("nullness")
    public final void updateValidation(
            final String text,
            final Object key) {
        this.refresh(false, Boolean.FALSE);

        if (key == InputDataValidator.MessageKey.USERNAME) {
            this.txtUsername.requestFocus();
        }

        if (key == InputDataValidator.MessageKey.PASSWORD) {
            this.txtPassword.requestFocus();
        }

        if (key == InputDataValidator.MessageKey.SIGNATURE) {
            this.txtSignature.requestFocus();
        }

        if (key == InputDataValidator.MessageKey.DATERANGE) {
            this.dateRanger.getEndField().requestFocus();
        }

        ValidationComponentUtils
        .updateComponentTreeMandatoryAndBlankBackground(this.jpanel);

        final Component parentComponent;
        if (this.isVisible()) {
            parentComponent = this;
        } else {
            parentComponent = null;
        }
        final JLabel errorLabel = new JLabel(text);
        errorLabel.setLabelFor(null);
        JOptionPane.showMessageDialog(
                parentComponent,
                errorLabel,
                null, // no title
                JOptionPane.ERROR_MESSAGE);
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
