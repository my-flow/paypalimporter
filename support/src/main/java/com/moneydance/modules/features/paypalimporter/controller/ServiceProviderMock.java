package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

import java.util.Date;

public final class ServiceProviderMock implements ServiceProvider {
    @Override
    public void callCheckCurrencyService(
            final String username,
            final char[] password,
            final String signature,
            final RequestHandler<CurrencyCodeType> requestHandler) {
        // ignore
    }

    @Override
    public void callTransactionSearchService(
            final String username,
            final char[] password,
            final String signature,
            final Date startDate,
            final Date endDate,
            final CurrencyCodeType currencyCode,
            final RequestHandler<PaymentTransactionSearchResultType> requestHandler) {
        // ignore
    }

    @Override
    public void shutdownNow() {
        // ignore
    }
}
