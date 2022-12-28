package com.moneydance.modules.features.paypalimporter.controller;

import com.infinitekind.moneydance.model.AccountBook;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.service.MockServiceResultFactory;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

public final class CheckCurrencyRequestHandlerTest {

    private MockServiceResultFactory mockServiceResultFactory;
    private AbstractRequestHandler<CurrencyCodeType> handler;
    private ViewController viewController;
    private Localizable localizable;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.mockServiceResultFactory = new MockServiceResultFactory(supportComponent.settings());

        this.viewController = supportComponent.viewController();
        this.localizable = supportComponent.localizable();

        this.handler = new CheckCurrencyRequestHandler(
                this.viewController,
                new StubContextFactory().getContext().getAccountBook(),
                UUID.randomUUID().toString(),
                this.localizable);
    }

    @Test
    public void testServiceCallFailed() {
        ServiceResult<CurrencyCodeType> result =
                this.mockServiceResultFactory.createFailedServiceResult();
        this.handler.serviceCallFinished(result);
    }

    @Test(expected = RuntimeException.class)
    public void testServiceCallSucceededEmptyResult() {
        ServiceResult<CurrencyCodeType> result =
                this.mockServiceResultFactory.createEmptyServiceResult();
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededValidSingleResult() {
        ServiceResult<CurrencyCodeType> result =
                this.mockServiceResultFactory.createValidSingleServiceResult(
                        MockServiceResultFactory.createCompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededMultipleResults() {
        ServiceResult<CurrencyCodeType> result =
                this.mockServiceResultFactory.createMultipleServiceResult(
                        MockServiceResultFactory.createIncompleteCurrencyCodeType(),
                        MockServiceResultFactory.createIncompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }

    @Test
    public void testServiceCallSucceededNoExistingAccount() {
        this.handler = new CheckCurrencyRequestHandler(
                this.viewController,
                new StubAccountBook(AccountBook.fakeAccountBook()),
                UUID.randomUUID().toString(),
                this.localizable);

        ServiceResult<CurrencyCodeType> result =
                this.mockServiceResultFactory.createValidSingleServiceResult(
                        MockServiceResultFactory.createCompleteCurrencyCodeType());
        this.handler.serviceCallSucceeded(result);
    }
}
