// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.AccountBook;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.service.MockServiceResultFactory;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class CheckCurrencyRequestHandlerTest {

    private AbstractRequestHandler<CurrencyCodeType> handler;

    @Before
    public void setUp() {
        StubContextFactory factory = new StubContextFactory();
        ViewController viewController = new ViewControllerMock();

        this.handler = new CheckCurrencyRequestHandler(
                viewController,
                factory.getContext().getAccountBook(),
                -1);
    }

    @Test
    public void testServiceCallFailed() {
        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createFailedServiceResult();
        this.handler.serviceCallFinished(result);
    }

    @Test(expected = RuntimeException.class)
    public void testServiceCallSucceededEmptyResult() {
        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededValidSingleResult() {
        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createValidSingleServiceResult(
                        MockServiceResultFactory.createCompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededMultipleResults() {
        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createMultipleServiceResult(
                        MockServiceResultFactory.createIncompleteCurrencyCodeType(),
                        MockServiceResultFactory.createIncompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededNoExistingAccount() {
        this.handler = new CheckCurrencyRequestHandler(
                new ViewControllerMock(),
                new StubAccountBook(AccountBook.fakeAccountBook()),
                -1);

        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createValidSingleServiceResult(
                        MockServiceResultFactory.createCompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }
}
