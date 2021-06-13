// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

import java.util.Date;

/**
 * Facade for initiating service calls. Also supports shutdown.
 *
 * @author Florian J. Breunig
 */
public interface ServiceProvider {

    void callCheckCurrencyService(
        String username,
        char[] password,
        String signature,
        RequestHandler<CurrencyCodeType> requestHandler);

    void callTransactionSearchService(
        String username,
        char[] password,
        String signature,
        Date startDate,
        Date endDate,
        CurrencyCodeType currencyCode,
        RequestHandler<PaymentTransactionSearchResultType> requestHandler);

    /**
     * Shuts down all running requests. Can be called anytime.
     */
    void shutdownNow();

}
