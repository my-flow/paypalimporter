// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.OnlineTxn;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.presentation.WizardHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class TransactionSearchIteratorTest {

    private AccountBook accountBook;
    private Settings settings;
    private InputData inputData;
    private TransactionSearchIterator iterator;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        ServiceProvider serviceProvider = supportComponent.serviceProvider();

        this.accountBook = supportComponent.accountBook().getWrappedOriginal();
        this.settings = supportComponent.settings();

        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.inputData = new InputData(
                "mock username",
                password,
                "mock signature",
                "mock account ID");

        StubContextFactory factory = new StubContextFactory();
        this.iterator = new TransactionSearchIterator(
                new ViewControllerMock(),
                factory.getContext().getAccountBook(),
                serviceProvider,
                inputData,
                this.accountBook.getRootAccount().getCurrencyType(),
                CurrencyCodeType.USD,
                this.settings.getErrorCodeSearchWarning(),
                this.settings.getDateFormat(),
                supportComponent.localizable());
    }

    @Test
    public void testCallTransactionSearchService() {
        this.iterator.callTransactionSearchService();
    }

    @Test
    public void testStartWizard() {
        this.iterator.startWizard();
        this.iterator.cancel();
    }

    @Test
    public void testCancel() {
        this.iterator.cancel();
    }

    @Test
    public void testProceed() {
        this.iterator.proceed(this.inputData);
    }

    @Test
    public void testUnlockNoSearchWarning() {
        this.iterator.unlock(
                "stub text",
                "stub key");
    }

    @Test
    public void testUnlockWithSearchWarning() {
        this.iterator.unlock(
                "stub text",
                this.settings.getErrorCodeSearchWarning());
    }

    @Test
    public void testCurrencyChecked() {
        this.iterator.currencyChecked(null,  null,  null);
    }

    @Test
    public void testTransactionsImportedWithSearchWarning() {
        this.iterator.transactionsImported(
                Collections.emptyList(),
                new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong())),
                null,
                this.settings.getErrorCodeSearchWarning());
    }

    @Test
    public void testTransactionsImportedNoSearchWarningEmpty() {
        this.iterator.transactionsImported(
                Collections.emptyList(),
                null,
                this.accountBook.getRootAccount(),
                null);
    }

    @Test
    public void testTransactionsImportedNoSearchWarningFilled() {
        final List<OnlineTxn> onlineTxns = new LinkedList<>();
        onlineTxns.add(this.accountBook.getRootAccount().getDownloadedTxns().newTxn());
        this.iterator.transactionsImported(
                onlineTxns,
                null,
                this.accountBook.getRootAccount(),
                null);
    }

    @Test
    public void testShowHelp() {
        try {
            this.iterator.showHelp();
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testRefreshAccounts() {
        this.iterator.refreshAccounts(null);
    }

    @Test
    public void testUpdate() {
        for (WizardHandler.ExecutedAction action : WizardHandler.ExecutedAction.values()) {
            this.iterator.update(new Observable(), action);
        }
    }
}
