package com.moneydance.modules.features.paypalimporter.filter;

import java.util.function.Predicate;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

final class RemovedTemporaryHoldFilter implements Predicate<PaymentTransactionSearchResultType> {

    private static final String TYPE = "...";

    @Override
    public boolean test(final PaymentTransactionSearchResultType result) {
        return TYPE.equals(result.getType());
    }
}
