// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.model.Account;
import com.moneydance.apps.md.model.OnlineTxn;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class ViewControllerImplTest {

    private ViewController viewController;
    private Account account;

    @Before
    public void setUp() {
        StubContextFactory factory = new StubContextFactory();
        this.account = factory.getContext().getRootAccount();

        this.viewController = new ViewControllerImpl(
                factory.getContext(),
                Helper.INSTANCE.getTracker(0));
        this.viewController.startWizard();
    }

    @Test
    public void testStartWizard() {
        this.viewController.startWizard();
    }

    @Test
    public void testCancel() {
        this.viewController.cancel();
    }

    @Test
    public void testProceed() {
        this.viewController.proceed();
    }

    @Test
    public void testUnlock() {
        this.viewController.unlock("stub text", "stub key");
    }

    @Test
    public void testCurrencyCheckedEmpty() {
        this.viewController.currencyChecked(
                null,
                CurrencyCodeType.USD,
                Collections.<CurrencyCodeType> emptyList());
    }

    @Test
    public void testCurrencyCheckedFilled() {
        List<CurrencyCodeType> currencyCodes = new LinkedList<CurrencyCodeType>();
        currencyCodes.add(CurrencyCodeType.USD);
        currencyCodes.add(CurrencyCodeType.EUR);
        this.viewController.currencyChecked(
                null,
                CurrencyCodeType.USD,
                currencyCodes);
    }

    @Test
    public void testTransactionsImportedEmptyNoErrorCode() {
        this.viewController.transactionsImported(
                Collections.<OnlineTxn> emptyList(),
                null,
                this.account,
                null);
    }

    @Test
    public void testTransactionsImportedEmptyWithErrorCode() {
        this.viewController.transactionsImported(
                Collections.<OnlineTxn> emptyList(),
                null,
                this.account,
                Helper.INSTANCE.getSettings().getErrorCodeSearchWarning());
    }

    @Test
    public void testTransactionsImportedFilled() {
        final List<OnlineTxn> onlineTxns = new LinkedList<OnlineTxn>();
        onlineTxns.add(this.account.getDownloadedTxns().newTxn());
        this.viewController.transactionsImported(
                onlineTxns,
                null,
                this.account,
                null);
    }

    @Test
    public void testShowHelp() {
        this.viewController.showHelp();
    }

    @Test
    public void testRefreshAccounts() {
        this.viewController.refreshAccounts(-1);
    }

    @Test
    public void testUpdate() {
        for (WizardHandler.ExecutedAction action : WizardHandler.ExecutedAction.values()) {
            this.viewController.update(new Observable(), action);
        }
    }
}
