// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.Account;
import com.infinitekind.moneydance.model.DateRange;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.model.InputData;
import com.moneydance.modules.features.paypalimporter.util.Settings;

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

    private ViewController viewController;
    private InputData validInputData;
    private InputData invalidInputData;
    private Account account;
    private Settings settings;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.account = supportComponent.context().getRootAccount().getSubAccount(0);
        this.settings = supportComponent.settings();

        ViewControllerImpl viewControllerImpl = new ViewControllerImpl(
                supportComponent.context(),
                new ServiceProviderMock(),
                supportComponent.dateConverter(),
                supportComponent.accountBook(),
                supportComponent.accountFilter(),
                supportComponent.settings(),
                supportComponent.preferences(),
                supportComponent.localizable());
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        String accountId = this.account.getUUID();

        this.validInputData = new InputData("mock username", password, "mock signature",
                accountId, new DateRange());
        this.invalidInputData = new InputData("", password, "",
                accountId, new DateRange());

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
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();

        ViewControllerImpl viewControllerImpl = new ViewControllerImpl(
                supportComponent.context(),
                new ServiceProviderMock(),
                supportComponent.dateConverter(),
                supportComponent.accountBook(),
                supportComponent.accountFilter(),
                supportComponent.settings(),
                supportComponent.preferences(),
                supportComponent.localizable());
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
                this.settings.getErrorCodeSearchWarning());
    }
}
