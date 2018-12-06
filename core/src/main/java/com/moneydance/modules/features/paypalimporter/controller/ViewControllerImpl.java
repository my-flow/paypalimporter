// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

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

import java.awt.Frame;
import java.awt.Image;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.BoundedRangeModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
    private final FeatureModuleContext context;
    private final ServiceProvider serviceProvider;
    private final IAccountBookFactory accountBookFactory;
    @Nullable private WizardHandler wizard;
    @Nullable private InputData inputData;

    public ViewControllerImpl(final FeatureModuleContext argContext, final ServiceProvider argServiceProvider) {
        this.prefs              = Helper.INSTANCE.getPreferences();
        this.localizable        = Helper.INSTANCE.getLocalizable();
        this.accountBookFactory = AccountBookFactoryImpl.INSTANCE;
        this.context            = argContext;
        this.serviceProvider    = argServiceProvider;
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
                assert this.wizard != null : "@AssumeAssertion(nullness)";
                this.wizard.refresh(false, Boolean.TRUE);
                this.proceed(this.wizard.getInputData());
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
            this.wizard.addComponentListener(
                    new ComponentDelegateListener(
                        this.accountBookFactory.createAccountBook(this.context).orElseThrow(AssertionError::new),
                            this));
        } else if (this.wizard.isVisible()) {
            this.wizard.setVisible(true);
            return;
        }
        this.wizard.pack();

        int accountId = this.prefs.getAccountId();
        if (accountId < 0) {
            final MainFrame mainFrame = (MainFrame) mdGUI.getTopLevelFrame();
            if (mainFrame != null) {
                final Account selectedAccount = mainFrame.getSelectedAccount();
                if (selectedAccount != null) {
                    accountId = selectedAccount.getAccountNum();
                }
            }
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
            this.unlock();
        } else {
            this.wizard.setVisible(false);
        }
    }

    @Override
    public void proceed(final InputData argInputData) {
        LOG.config(argInputData.toString());
        Validator<InputData> inputValidator = new InputDataValidator();
        ValidationResult result = inputValidator.validate(argInputData);
        if (result.hasErrors()) {
            this.unlock(
                    result.getErrors().get(0).formattedText(),
                    result.getErrors().get(0).key());
            return;
        }

        this.inputData = argInputData;
        final IAccountBook accountBook = this.accountBookFactory.createAccountBook(this.context)
                .orElseThrow(AssertionError::new);

        final String username = argInputData.getUsername().orElseThrow(AssertionError::new);
        final char[] password = argInputData.getPassword(false).orElseThrow(AssertionError::new);
        final String signature = argInputData.getSignature().orElseThrow(AssertionError::new);

        this.serviceProvider.callCheckCurrencyService(
                username,
                password,
                signature,
                new CheckCurrencyRequestHandler(
                    this,
                    accountBook,
                    argInputData.getAccountId()));
    }

    @Override
    public void unlock() {
        this.inputData = null;
        assert this.wizard != null : "@AssumeAssertion(nullness)";
        this.wizard.refresh(false, Boolean.FALSE);
    }

    @Override
    public void unlock(final String text, final Object key) {
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
                this.inputData.getUsername().orElseThrow(AssertionError::new))) {

            final String message =
                    this.localizable.getQuestionMessageMultipleCurrencies(
                            currencyCode.name(),
                            currencyCodes
                                    .stream()
                                    .map(CurrencyCodeType::getValue)
                                    .collect(Collectors.toList()));
            final JLabel confirmationLabel = new JLabel(message);
            confirmationLabel.setLabelFor(null);
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
                LOG.info("Continue");
                this.importTransactions(currencyType, currencyCode);
            } else {
                LOG.info("Cancel");
                this.unlock();
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
                this.accountBookFactory.createAccountBook(this.context).orElseThrow(AssertionError::new),
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
            this.prefs.setUsername(accountId, input.getUsername().orElseThrow(AssertionError::new));
            this.prefs.setPassword(accountId, input.getPassword(true).orElseThrow(AssertionError::new));
            this.prefs.setSignature(accountId, input.getSignature().orElseThrow(AssertionError::new));
            this.prefs.setAccountId(accountId);
            this.prefs.assignBankingFI(accountId);

            this.unlock();
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
    public void showHelp() throws MalformedURLException {
        this.context.showURL(this.localizable.getUrlHelp().toExternalForm());
        this.cancel();
    }

    @Override
    public void refreshAccounts(final int accountId) {
        LOG.config("Refreshing accounts");
        final IAccountBook accountBook = this.accountBookFactory.createAccountBook(this.context)
                .orElseThrow(AssertionError::new);
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

    void setInputData(@Nonnull final InputData argInputData) {
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
