// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.controller.Util;
import com.moneydance.apps.md.model.time.DateRangeOption;

/**
 * @author Florian J. Breunig
 */
public final class ServiceProviderTest {

    private ServiceProvider serviceProvider;

    @Before
    public void setUp() throws Exception {
        new StubContextFactory();
        this.serviceProvider = new ServiceProvider();
    }

    @Test
    public void testCallCheckCurrencyService() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.serviceProvider.callCheckCurrencyService(
                "mock username",
                password,
                "mock signature",
                new RequestHandler<CurrencyCodeType>() {
                    @Override
                    public void serviceCallFinished(
                            final ServiceResult<CurrencyCodeType> serviceResult) {
                        assertThat(serviceResult, notNullValue());
                    }
                });
    }

    @Test
    public void testCallTransactionSearchService() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.serviceProvider.callTransactionSearchService(
                "mock username",
                password,
                "mock signature",
                Util.calculateDateRange(DateRangeOption.DR_LAST_12_MONTHS),
                CurrencyCodeType.USD,
                true,
                new RequestHandler<PaymentTransactionSearchResultType>() {
                    @Override
                    public void serviceCallFinished(
                            final ServiceResult<PaymentTransactionSearchResultType> serviceResult) {
                        assertThat(serviceResult, notNullValue());
                    }
                });
    }

    @Test
    public void testShutdownNow() {
        assertThat(this.serviceProvider.shutdownNow().isEmpty(), is(true));
    }

}
