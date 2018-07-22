// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.DateRange;
import com.moneydance.apps.md.controller.StubAccountBookFactory;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class ViewControllerImplTest {

    private StubContextFactory factory;
    private ViewController viewController;
    private InputData validInputData;
    private InputData invalidInputData;
    private Account account;

    @Before
    public void setUp() {
        this.factory = new StubContextFactory();
        Helper.INSTANCE.setPreferences(
                new StubAccountBookFactory(
                        this.factory.getContext().getAccountBook()));
        this.account = factory.getContext().getRootAccount().getSubAccount(0);

        ViewControllerImpl viewControllerImpl = new ViewControllerImpl(this.factory.getContext(), new ServiceProviderMock());
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.validInputData = new InputData("mock username", password, "mock signature",
                this.account.getAccountNum(), new DateRange());
        this.invalidInputData = new InputData("", password, "",
                this.account.getAccountNum(), new DateRange());

        viewControllerImpl.setInputData(validInputData);
        this.viewController = viewControllerImpl;
    }

    @Test
    public void testProceedSuccessfully() {
        this.viewController.proceed(this.validInputData);
    }

    @Test
    public void testCurrencyCheckedEmpty() {
        this.viewController.currencyChecked(
                this.account.getCurrencyType(),
                CurrencyCodeType.USD,
                Collections.emptyList());
    }

    @Test
    public void testCurrencyCheckedFilled() {
        ViewControllerImpl viewControllerImpl = new ViewControllerImpl(this.factory.getContext(), new ServiceProviderMock());
        viewControllerImpl.setInputData(this.invalidInputData);

        List<CurrencyCodeType> currencyCodes = new LinkedList<>();
        currencyCodes.add(CurrencyCodeType.USD);
        currencyCodes.add(CurrencyCodeType.EUR);
        viewControllerImpl.currencyChecked(
                this.account.getCurrencyType(),
                CurrencyCodeType.USD,
                currencyCodes);
    }

    @Test
    public void testTransactionsImportedEmptyWithErrorCode() {
        this.viewController.transactionsImported(
                Collections.emptyList(),
                null,
                this.account,
                Helper.INSTANCE.getSettings().getErrorCodeSearchWarning());
    }
}
