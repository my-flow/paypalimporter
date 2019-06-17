// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
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
    private MockServiceResultFactory mockServiceResultFactory;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();

        this.mockServiceResultFactory =
                new MockServiceResultFactory(supportComponent.settings());

        StubContextFactory factory = new StubContextFactory();

        this.handler = new TransactionSearchRequestHandler(
                supportComponent.viewController(),
                factory.getContext().getAccountBook(),
                supportComponent.settings().getDateFormat(),
                supportComponent.localizable());
    }

    @Test
    public void testServiceCallSucceededEmptyResult() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                this.mockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededValidSingleResult() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                this.mockServiceResultFactory.createValidSingleServiceResult(
                        this.mockServiceResultFactory.createCompletePaymentTransactionSearchResultType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededMultipleResults() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                this.mockServiceResultFactory.createMultipleServiceResult(
                        this.mockServiceResultFactory.createIncompletePaymentTransactionSearchResultType(),
                        this.mockServiceResultFactory.createInvalidPaymentTransactionSearchResultType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallFinished() {
        ServiceResult<PaymentTransactionSearchResultType> result =
                this.mockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallFinished(result);
    }
}
