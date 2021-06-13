// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * @author Florian J. Breunig
 */
public final class MockServiceResultFactory {

    private final Settings settings;

    public MockServiceResultFactory(final Settings argSettings) {
        this.settings = argSettings;
    }

    public <V> ServiceResult<V> createFailedServiceResult() {
        String errorMessage = "stub error message";
        return new ServiceResult<>(null, null, errorMessage);
    }

    public <V> ServiceResult<V> createEmptyServiceResult() {
        return new ServiceResult<>(Collections.<V>emptyList(), null, null);
    }

    public <V> ServiceResult<V> createValidSingleServiceResult(
            final V resultType) {
        return new ServiceResult<>(
                Collections.singletonList(resultType),
                null,
                null);
    }

    public <V> ServiceResult<V> createMultipleServiceResult(
            final V resultType1, final V resultType2) {
        final List<V> results = new LinkedList<>();
        results.add(resultType1);
        results.add(resultType2);

        return new ServiceResult<>(
                results,
                null,
                null);
    }

    public static CurrencyCodeType createCompleteCurrencyCodeType() {
        return CurrencyCodeType.USD;
    }

    public static CurrencyCodeType createIncompleteCurrencyCodeType() {
        return CurrencyCodeType.CUSTOMCODE;
    }

    public PaymentTransactionSearchResultType
    createCompletePaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        DateFormat dateFormat = this.settings.getDateFormat();
        resultType.setTimestamp(dateFormat.format(new Date()));

        BasicAmountType grossAmount = new BasicAmountType();
        grossAmount.setValue("0.00");
        resultType.setGrossAmount(grossAmount);

        resultType.setPayer("stub payer name");

        return resultType;
    }

    public PaymentTransactionSearchResultType
    createIncompletePaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        DateFormat dateFormat = this.settings.getDateFormat();
        resultType.setTimestamp(dateFormat.format(new Date()));

        // gross amount is missing

        // payer property is missing

        return resultType;
    }

    public PaymentTransactionSearchResultType
    createInvalidPaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        // gross amount is missing
        resultType.setPayer("stub payer name");
        resultType.setTimestamp("invalid timestamp");

        return resultType;
    }
}
