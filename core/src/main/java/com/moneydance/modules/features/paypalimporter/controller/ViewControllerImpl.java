// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.OnlineTxn;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.controller.Util;
import com.moneydance.apps.md.view.gui.AccountListModel;
import com.moneydance.apps.md.view.gui.MainFrame;
import com.moneydance.apps.md.view.gui.MoneydanceGUI;
import com.moneydance.apps.md.view.gui.OnlineManager;
import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
import com.moneydance.modules.features.paypalimporter.model.AccountBookFactoryImpl;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.model.InputDataValidator;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Tracker;

import java.awt.Frame;
import java.awt.Image;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.swing.BoundedRangeModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * Default implementation of the control flow interface.
 *
 * @author Florian J. Breunig
 */
public final class ViewControllerImpl implements ViewController {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            ViewControllerImpl.class.getName());

    private final Preferences prefs;
    private final Localizable localizable;
    private final Tracker tracker;
    private final FeatureModuleContext context;
    private final ServiceProvider serviceProvider;
    private final IAccountBookFactory accountBookFactory;
    @Nullable private WizardHandler wizard;
    @Nullable private InputData inputData;

    public ViewControllerImpl(
            final FeatureModuleContext argContext,
            final Tracker argTracker) {
        this.prefs              = Helper.INSTANCE.getPreferences();
        this.localizable        = Helper.INSTANCE.getLocalizable();
        this.accountBookFactory = AccountBookFactoryImpl.INSTANCE;
        this.tracker            = argTracker;
        this.context            = argContext;
        this.serviceProvider    = new ServiceProvider();
    }

    @Override
    @SuppressWarnings("nullness")
    public void startWizard() {
        this.update(null, WizardHandler.ExecutedAction.START_WIZARD);
    }

    @Override
    @SuppressWarnings("nullable")
    public void update(final Observable observable, final Object arg) {
        try {
            WizardHandler.ExecutedAction action =
                    (WizardHandler.ExecutedAction) arg;
            switch (action) {
            case START_WIZARD:
                this.initAndShowWizard();
                break;
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
        } catch (Throwable t) {
            final String message = t.getMessage();
            if (message != null) {
                LOG.log(Level.SEVERE, message, t);
            }
            this.tracker.track(ExceptionUtils.getStackTrace(t));
        }
    }

    @SuppressWarnings({"deprecation", "nullness"})
    private void initAndShowWizard() {
        Helper.INSTANCE.setChanged();
        Helper.INSTANCE.notifyObservers(Boolean.TRUE);

        final MoneydanceGUI mdGUI = this.getMoneydanceGUI();

        if (this.wizard == null) {
            final Frame owner = mdGUI.getTopLevelFrame();
            this.wizard = new WizardHandler(owner, mdGUI, this);
            this.wizard.addComponentListener(new ComponentDelegateListener(
                    this.accountBookFactory.createAccountBook(
                            this.context), this));
            this.tracker.track(Tracker.EventName.DISPLAY);
        } else if (this.wizard.isVisible()) {
            this.wizard.setVisible(true);
            return;
        }
        this.wizard.pack();

        int accountId = -1;
        final MainFrame mainFrame = (MainFrame) mdGUI.getTopLevelFrame();
        if (mainFrame != null && mainFrame.getSelectedAccount() != null) {
            accountId = mainFrame.getSelectedAccount().getAccountNum();
        }

        final InputData newUserData = new InputData(
            this.prefs.getUsername(accountId),
            this.prefs.getPassword(accountId),
            this.prefs.getSignature(accountId),
            accountId);

        this.wizard.setInputData(newUserData);
        this.refreshAccounts(newUserData.getAccountId());
        this.wizard.setLocationRelativeTo(null);
        this.wizard.setVisible(true);
    }

    @Override
    public void cancel() {
        this.serviceProvider.shutdownNow();
        assert this.wizard != null : "@AssumeAssertion(nullness)";
        if (this.wizard.isLoading()) {
            this.unlock(null, null);
        } else {
            assert this.wizard != null : "@AssumeAssertion(nullness)";
            this.wizard.setVisible(false);
        }
    }

    @Override
    public void proceed() {
        assert this.wizard != null : "@AssumeAssertion(nullness)";
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
        final IAccountBook accountBook = this.accountBookFactory.createAccountBook(this.context);
        assert accountBook != null : "@AssumeAssertion(nullness)";

        final String username = newInputData.getUsername();
        final char[] password = newInputData.getPassword(false);
        final String signature = newInputData.getSignature();
        assert username != null : "@AssumeAssertion(nullness)";
        assert password != null : "@AssumeAssertion(nullness)";
        assert signature != null : "@AssumeAssertion(nullness)";

        this.serviceProvider.callCheckCurrencyService(
                username,
                password,
                signature,
                new CheckCurrencyRequestHandler(
                    this,
                    accountBook,
                    newInputData.getAccountId()));
    }

    @Override
    public void unlock(@Nullable final String text, @Nullable final Object key) {
        this.inputData = null;
        assert this.wizard != null : "@AssumeAssertion(nullness)";
        this.wizard.updateValidation(text, key);
    }

    @Override
    @SuppressWarnings("nullness")
    public void currencyChecked(
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode,
            final List<CurrencyCodeType> currencyCodes) {

        if (currencyCodes.size() > 1 && !this.prefs.hasUsedCombination(
                this.inputData.getAccountId(),
                this.inputData.getUsername())) {

            final String message =
                    this.localizable.getQuestionMessageMultipleCurrencies(
                            currencyCode.name(),
                            currencyCodes.toArray());
            final Object confirmationLabel = new JLabel(message);
            final Image image = Helper.INSTANCE.getSettings().getIconImage();
            final Icon icon = new ImageIcon(image);
            final Object[] options = {
                    this.localizable.getLabelContinueButton(),
                    this.getMoneydanceGUI().getStr("cancel")
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
                this.importTransactions(currencyType, currencyCode);
            } else {
                LOG.info(String.format("Cancel"));
                this.unlock(null, null);
            }
        } else {
            this.importTransactions(currencyType, currencyCode);
        }
    }

    @SuppressWarnings("nullness")
    private void importTransactions(
            final CurrencyType currencyType,
            final CurrencyCodeType currencyCode) {

        TransactionSearchIterator iter = new TransactionSearchIterator(
                this,
                this.accountBookFactory.createAccountBook(this.context),
                this.serviceProvider,
                this.inputData,
                currencyType,
                currencyCode);
        iter.callTransactionSearchService();
    }

    @Override
    public void transactionsImported(
            final List<OnlineTxn> onlineTxns,
            final Date currentStartDate,
            @Nullable final Account account,
            @Nullable final String errorCode) {

        final InputData input = this.inputData;
        assert input != null : "@AssumeAssertion(nullness)";
        assert account != null : "@AssumeAssertion(nullness)";

        if (!onlineTxns.isEmpty()) {
            // refresh progress bar
            final Date endDate = DateUtils.ceiling(
                    Util.convertIntDateToLong(
                            onlineTxns.get(0).getDatePostedInt()),
                            Calendar.DATE);
            final BoundedRangeModel model = DateConverter.getBoundedRangeModel(
                    input.getStartDate(),
                    endDate,
                    currentStartDate);
            assert this.wizard != null : "@AssumeAssertion(nullness)";
            this.wizard.setBoundedRangeModel(model);
        }
        if (errorCode == null) {
            final int accountId = account.getAccountNum();
            this.prefs.setUsername(accountId, input.getUsername());
            this.prefs.setPassword(accountId, input.getPassword(true));
            this.prefs.setSignature(accountId, input.getSignature());
            this.prefs.assignBankingFI(accountId);

            this.unlock(null, null);
            assert this.wizard != null : "@AssumeAssertion(nullness)";
            this.wizard.setVisible(false);

            final MoneydanceGUI mdGUI = this.getMoneydanceGUI();
            if (mdGUI != null) {
                final OnlineManager manager = mdGUI.getOnlineManager();
                manager.processDownloadedTxns(account);
            }
        }
    }

    @Override
    public void showHelp() {
        this.context.showURL(this.localizable.getUrlHelp().toExternalForm());
        this.cancel();
    }

    @Override
    public void refreshAccounts(final int accountId) {
        LOG.config("Refreshing accounts");
        final IAccountBook accountBook =
                this.accountBookFactory.createAccountBook(this.context);
        assert accountBook != null : "@AssumeAssertion(nullness)";
        final AccountListModel accountModel = new AccountListModel(
                accountBook.getRootAccount());
        accountModel.setShowBankAccounts(true);
        accountModel.setShowCreditCardAccounts(true);
        accountModel.setShowInvestAccounts(true);
        accountModel.setShowAssetAccounts(true);
        accountModel.setShowLiabilityAccounts(true);
        accountModel.setShowLoanAccounts(true);
        final Account selectedAccount = accountBook.getAccountByNum(accountId);
        if (selectedAccount != null
                && accountModel.isAccountViewable(selectedAccount)) {
            accountModel.setSelectedAccount(selectedAccount);
        }
        final WizardHandler wizardHandler = this.wizard;
        assert wizardHandler != null : "@AssumeAssertion(nullness)";
        wizardHandler.setAccounts(accountModel);
        wizardHandler.invalidate();
        wizardHandler.validate();
    }

    void setInputData(final InputData argInputData) {
        this.inputData = argInputData;
    }

    private MoneydanceGUI getMoneydanceGUI() {
        // Using undocumented feature.
        Main main = (Main) this.context;
        if (main == null) {
            throw new IllegalStateException("FeatureModuleContext is null");
        }
        return (MoneydanceGUI) main.getUI();
    }
}
