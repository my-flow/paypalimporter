// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

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
        final String username,
        final char[] password,
        final String signature,
        final RequestHandler<CurrencyCodeType> requestHandler);

    void callTransactionSearchService(
        final String username,
        final char[] password,
        final String signature,
        final Date startDate,
        final Date endDate,
        final CurrencyCodeType currencyCode,
        final RequestHandler<PaymentTransactionSearchResultType>
                requestHandler);

    /**
     * Shuts down all running requests. Can be called anytime.
     */
    void shutdownNow();

}
