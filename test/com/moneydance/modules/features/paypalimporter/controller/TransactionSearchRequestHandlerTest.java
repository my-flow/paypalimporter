// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.service.MockServiceResultFactory;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
public final class TransactionSearchRequestHandlerTest {

    private AbstractRequestHandler<PaymentTransactionSearchResultType> handler;

    @Before
    public void setUp() {
        StubContextFactory factory = new StubContextFactory();
        ViewController viewController = new ViewControllerMock();

        this.handler = new TransactionSearchRequestHandler(
                viewController,
                factory.getContext().getRootAccount(),
                -1,
                factory.getContext().getRootAccount().getCurrencyType());
    }

    @Test
    public void testServiceCallSucceededEmptyResult() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                MockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededValidSingleResult() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                MockServiceResultFactory.createValidSingleServiceResult(
                        MockServiceResultFactory.createCompletePaymentTransactionSearchResultType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededMultipleResults() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                MockServiceResultFactory.createMultipleServiceResult(
                        MockServiceResultFactory.createIncompletePaymentTransactionSearchResultType(),
                        MockServiceResultFactory.createInvalidPaymentTransactionSearchResultType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallFinished() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                MockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallFinished(result);
    }
}
