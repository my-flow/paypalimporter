// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class ServiceProviderTest {

    private ServiceProvider serviceProvider;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();

        this.serviceProvider = supportComponent.serviceProvider();
    }

    @Test
    public void testCallCheckCurrencyService() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.serviceProvider.callCheckCurrencyService(
                "mock username",
                password,
                "mock signature",
                serviceResult -> assertThat(serviceResult, notNullValue()));
    }

    @Test
    public void testCallTransactionSearchService() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, +1);

        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        this.serviceProvider.callTransactionSearchService(
                "mock username",
                password,
                "mock signature",
                yesterday.getTime(),
                tomorrow.getTime(),
                CurrencyCodeType.USD,
                serviceResult -> assertThat(serviceResult, notNullValue()));
    }

    @Test
    public void testShutdownNow() {
        this.serviceProvider.shutdownNow();
    }
}
