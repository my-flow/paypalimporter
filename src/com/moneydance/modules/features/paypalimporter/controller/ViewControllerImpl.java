// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.apps.md.view.gui.AccountListModel;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.apps.md.view.gui.OnlineManager;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.modules.features.paypalimporter.util.Tracker;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class ViewControllerImpl implements ViewController {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            ViewControllerImpl.class.getName());

    private final Preferences           prefs;
    private final Localizable           localizable;
    private final Tracker               tracker;
    private final FeatureModuleContext  context;
    private final ServiceProvider       serviceProvider;
    private       WizardHandler         wizard;
    private       InputData             inputData;

    public ViewControllerImpl(
            final FeatureModuleContext argContext,
            final Tracker argTracker) {
        this.prefs           = Helper.INSTANCE.getPreferences();
        this.localizable     = Helper.getLocalizable();
        this.tracker         = argTracker;
        this.context         = argContext;
        this.serviceProvider = new ServiceProvider();
    }

    @Override
    public void update(final Observable observable, final Object arg) {
        WizardHandler.ExecutedAction action =
                (WizardHandler.ExecutedAction) arg;
        switch (action) {
        case SHOW_HELP:
            this.showHelp();
            break;
        case CANCEL:
            this.cancel();
            break;
        case PROCEED:
            this.proceed();
            break;
        default:
            throw new IllegalArgumentException(
                    String.format("case %s not defined", observable));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void startWizard() {

        if (this.context.getRootAccount() == null) {
            // this condition can only be true in Moneydance 2008
            // and older versions

            final MoneydanceGUI mdGUI = this.getMoneydanceGUI();
            final Component parentComponent = mdGUI.getTopLevelFrame();
            final Object errorLabel = new JLabel(
                    this.localizable.getErrorMessageRootAccountNull());
            JOptionPane.showMessageDialog(
                    parentComponent,
                    errorLabel,
                    null, // no title
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Helper.INSTANCE.setChanged();
        Helper.INSTANCE.notifyObservers(Boolean.TRUE);

        if (this.wizard == null) {
            final MoneydanceGUI mdGUI = this.getMoneydanceGUI();
            final Frame owner = mdGUI.getTopLevelFrame();
            this.wizard = new WizardHandler(owner, mdGUI, this);
            this.wizard.addComponentListener(
                    new ComponentDelegateListener(this.context, this));
            this.tracker.track(Tracker.EventName.DISPLAY);
        } else if (this.wizard.isVisible()) {
            this.wizard.setVisible(true);
            return;
        }
        this.wizard.pack();

        final InputData newUserData = new InputData(
                this.prefs.getUsername(),
                this.prefs.getPassword(),
                this.prefs.getSignature(),
                this.prefs.getAccountId());

        this.wizard.setInputData(newUserData);
        this.refreshAccounts(newUserData.getAccountId());
        this.wizard.setLocationRelativeTo(null);
        this.wizard.setVisible(true);
    }

    @Override
    public void cancel() {
        this.serviceProvider.shutdownNow();
        if (this.wizard.isLoading()) {
            this.unlock(null, null);
        } else {
            this.wizard.setVisible(false);
        }
    }

    @Override
    public void proceed() {
        this.wizard.refresh(false, Boolean.TRUE);

        final InputData newInputData = this.wizard.getInputData();
        LOG.config(newInputData.toString());
        Validator<InputData> inputValidator = new InputDataValidator();
        ValidationResult result = inputValidator.validate(newInputData);
        if (result.hasErrors()) {
            this.unlock(
                    result.getErrors().get(0).formattedText(),
                    result.getErrors().get(0).key());
            return;
        }

        this.inputData = newInputData;
        this.serviceProvider.callCheckCurrencyService(
                this.inputData.getUsername(),
                this.inputData.getPassword(false),
                this.inputData.getSignature(),
                new CheckCurrencyRequestHandler(
                        this,
                        this.context.getRootAccount(),
                        this.inputData.getAccountId()));
    }

    @Override
    public void unlock(final String text, final Object key) {
        this.inputData = null;
        this.wizard.updateValidation(text, key);
    }

    @Override
    public void currencyChecked(
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode,
            final List<CurrencyCodeType> currencyCodes) {

        this.prefs.setUsername(this.inputData.getUsername());
        this.prefs.setPassword(this.inputData.getPassword(false));
        this.prefs.setSignature(this.inputData.getSignature());
        this.prefs.setAccountId(this.inputData.getAccountId());

        if (currencyCodes.size() >= 0
                && !this.prefs.hasUsedImportCombination(
                        this.inputData.getUsername(),
                        this.inputData.getAccountId())) {

            final String message =
                    this.localizable.getQuestionMessageMultipleCurrencies(
                            currencyCode.name(),
                            currencyCodes.toArray());
            final Object confirmationLabel = new JLabel(message);
            final Image image = Settings.getIconImage();
            Icon icon  = null;
            if (image != null) {
                icon = new ImageIcon(image);
            }
            final Object[] options = {
                    "Continue",
                    "Cancel"
            };

            final int choice = JOptionPane.showOptionDialog(
                    this.wizard,
                    confirmationLabel,
                    null, // no title
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    icon,
                    options,
                    options[0]);

            if (choice == JOptionPane.OK_OPTION) {
                LOG.info(String.format("Continue"));
                this.prefs.setUsedImportCombination(
                        this.inputData.getUsername(),
                        this.inputData.getAccountId());
                this.importTransactions(currencyType, currencyCode);
            } else {
                LOG.info(String.format("Cancel"));
                this.unlock(null, null);
            }
        } else {
            this.importTransactions(currencyType, currencyCode);
        }
    }

    private void importTransactions(
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode) {

        TransactionSearchIterator iter = new TransactionSearchIterator(
                this,
                this.context.getRootAccount(),
                this.serviceProvider,
                this.inputData,
                currencyType,
                currencyCode);
        iter.callTransactionSearchService();
    }

    @Override
    public void transactionsImported(
            final List<OnlineTxn> onlineTxns,
            final Date argStartDate,
            final Account account,
            final String errorCode) {

        this.prefs.assignBankingFI(account.getAccountNum());

        this.unlock(null, null);
        this.wizard.setVisible(false);

        OnlineManager onlineMgr = new OnlineManager(this.getMoneydanceGUI());
        onlineMgr.processDownloadedTxns(account);
    }

    @Override
    public void showHelp() {
        this.context.showURL(this.localizable.getUrlHelp().toExternalForm());
        this.cancel();
    }

    @Override
    public void refreshAccounts(final int accountId) {
        LOG.config("Refreshing accounts");
        final RootAccount rootAccount = this.context.getRootAccount();
        final AccountListModel accountModel = new AccountListModel(rootAccount);
        accountModel.setShowBankAccounts(true);
        accountModel.setShowCreditCardAccounts(true);
        accountModel.setShowInvestAccounts(true);
        accountModel.setShowAssetAccounts(true);
        accountModel.setShowLiabilityAccounts(true);
        accountModel.setShowLoanAccounts(true);
        final Account selectedAccount = rootAccount.getAccountById(accountId);
        accountModel.setSelectedAccount(selectedAccount);
        this.wizard.setAccounts(accountModel);
        this.wizard.invalidate();
        this.wizard.validate();
    }

    private MoneydanceGUI getMoneydanceGUI() {
        // Using undocumented feature.
        Main main = (Main) this.context;
        MoneydanceGUI result;
        if (main == null) {
            result = null;
        } else {
            result = (MoneydanceGUI) main.getUI();
        }
        return result;
    }
}
