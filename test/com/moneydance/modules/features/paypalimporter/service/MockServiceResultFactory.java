// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

import com.moneydance.modules.features.paypalimporter.util.Helper;

/**
 * @author Florian J. Breunig
 */
public final class MockServiceResultFactory {

    /**
     * Restrictive constructor.
     */
    private MockServiceResultFactory() {
        // Prevents this class from being instantiated from the outside.
    }

    public static <V> ServiceResult<V> createFailedServiceResult() {
        String errorMessage = "stub error message";
        return new ServiceResult<V>(null, null, errorMessage);
    }

    public static <V> ServiceResult<V> createEmptyServiceResult() {
        return new ServiceResult<V>(Collections.<V>emptyList(), null, null);
    }

    public static <V> ServiceResult<V> createValidSingleServiceResult(
            final V resultType) {
        return new ServiceResult<V>(
                Collections.singletonList(resultType),
                null,
                null);
    }

    public static <V> ServiceResult<V> createMultipleServiceResult(
            final V resultType1, final V resultType2) {
        final List<V> results = new LinkedList<V>();
        results.add(resultType1);
        results.add(resultType2);

        return new ServiceResult<V>(
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

    public static PaymentTransactionSearchResultType
    createCompletePaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        DateFormat dateFormat = new SimpleDateFormat(
                Helper.INSTANCE.getSettings().getDatePattern(), Locale.US);
        resultType.setTimestamp(dateFormat.format(new Date()));

        BasicAmountType grossAmount = new BasicAmountType();
        grossAmount.setValue("0.00");
        resultType.setGrossAmount(grossAmount);

        resultType.setPayer("stub payer name");

        return resultType;
    }

    public static PaymentTransactionSearchResultType
    createIncompletePaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        DateFormat dateFormat = new SimpleDateFormat(
                Helper.INSTANCE.getSettings().getDatePattern(), Locale.US);
        resultType.setTimestamp(dateFormat.format(new Date()));

        // gross amount is missing

        // payer property is missing

        return resultType;
    }

    public static PaymentTransactionSearchResultType
    createInvalidPaymentTransactionSearchResultType() {

        PaymentTransactionSearchResultType resultType =
                new PaymentTransactionSearchResultType();

        // gross amount is missing
        resultType.setPayer("stub payer name");
        resultType.setTimestamp("invalid timestamp");

        return resultType;
    }
}
