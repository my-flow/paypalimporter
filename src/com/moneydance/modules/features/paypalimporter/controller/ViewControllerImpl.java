// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import java.awt.Frame;
import java.util.Observable;
import java.util.logging.Logger;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.apps.md.view.gui.AccountListModel;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.apps.md.view.gui.OnlineManager;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;
import com.moneydance.modules.features.paypalimporter.model.MutableInputData;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Tracker;

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
        this.localizable     = Helper.INSTANCE.getLocalizable();
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

        final InputData newUserData = new MutableInputData(
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
        if (this.inputData == null) {
            this.wizard.setVisible(false);
        } else {
            this.serviceProvider.shutdownNow();
            this.unlock(null, null);
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
            final boolean isPrimaryCurrency) {
        this.prefs.setUsername(this.inputData.getUsername());
        this.prefs.setPassword(this.inputData.getPassword(false));
        this.prefs.setSignature(this.inputData.getSignature());
        this.prefs.setAccountId(this.inputData.getAccountId());

        RootAccount rootAccount = this.context.getRootAccount();
        this.serviceProvider.callTransactionSearchService(
                this.inputData.getUsername(),
                this.inputData.getPassword(true),
                this.inputData.getSignature(),
                this.inputData.getDateRange(),
                currencyCode,
                isPrimaryCurrency,
                new TransactionSearchRequestHandler(
                        this,
                        rootAccount,
                        this.inputData.getAccountId(),
                        currencyType));
        this.inputData = null;
    }

    @Override
    public void transactionsImported(final Account account) {
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
