/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.controller;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.controller.AbstractRequestHandler;
import com.moneydance.modules.features.paypalimporter.controller.CheckCurrencyRequestHandler;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;
import com.moneydance.modules.features.paypalimporter.service.MockServiceResultFactory;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;

/**
 * @author Florian J. Breunig
 */
public final class CheckCurrencyRequestHandlerTest {

    private AbstractRequestHandler<CurrencyCodeType> handler;

    @Before
    public void setUp() throws Exception {
        StubContextFactory factory = new StubContextFactory();
        ViewController viewController = new ViewControllerMock();

        this.handler = new CheckCurrencyRequestHandler(
                viewController,
                factory.getContext().getRootAccount(),
                -1,
                null);
    }

    @Test
    public void testServiceCallFailed() {
        ServiceResult<CurrencyCodeType> result =
                MockServiceResultFactory.createFailedServiceResult();
        this.handler.serviceCallFinished(result);
    }

    @Test(expected=RuntimeException.class)
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
}
