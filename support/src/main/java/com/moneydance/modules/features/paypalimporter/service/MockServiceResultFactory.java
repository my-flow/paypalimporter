package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.moneydance.modules.features.paypalimporter.model.BasicAmount;
import com.moneydance.modules.features.paypalimporter.model.CurrencyCodeType;
import com.moneydance.modules.features.paypalimporter.model.PaymentTransactionSearchResultType;

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
        return CurrencyCodeType.fromValue("USD");
    }

    public static CurrencyCodeType createIncompleteCurrencyCodeType() {
        return CurrencyCodeType.UNKNOWN;
    }

    public PaymentTransactionSearchResultType
    createCompletePaymentTransactionSearchResultType() {

        DateFormat dateFormat = this.settings.getDateFormat();
        String timestamp = dateFormat.format(new Date());

        BasicAmount grossAmount = new BasicAmount("0.00", CurrencyCodeType.fromValue("USD"));

        return new PaymentTransactionSearchResultType(
                "stub payer name",
                "stub payer display name",
                timestamp,
                "stub transaction id",
                "stub status",
                "stub type",
                grossAmount);
    }

    public PaymentTransactionSearchResultType
    createIncompletePaymentTransactionSearchResultType() {

        DateFormat dateFormat = this.settings.getDateFormat();
        String timestamp = dateFormat.format(new Date());

        // gross amount is missing, payer property is missing
        return new PaymentTransactionSearchResultType(
                null,
                null,
                timestamp,
                null,
                null,
                null,
                null);
    }

    public PaymentTransactionSearchResultType
    createInvalidPaymentTransactionSearchResultType() {

        // gross amount is missing
        return new PaymentTransactionSearchResultType(
                "stub payer name",
                null,
                "invalid timestamp",
                null,
                null,
                null,
                null);
    }
}
