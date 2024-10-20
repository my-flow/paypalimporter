package com.moneydance.modules.features.paypalimporter.filter;

import java.util.function.Predicate;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

public final class NotRemovedTemporaryHoldFilter implements Predicate<PaymentTransactionSearchResultType> {

    private final Predicate<PaymentTransactionSearchResultType> removedTemporaryHoldFilter;

    public NotRemovedTemporaryHoldFilter() {
        this.removedTemporaryHoldFilter = new RemovedTemporaryHoldFilter();
    }

    @Override
    public boolean test(final PaymentTransactionSearchResultType result) {
        return this.removedTemporaryHoldFilter.negate().test(result);
    }
}
