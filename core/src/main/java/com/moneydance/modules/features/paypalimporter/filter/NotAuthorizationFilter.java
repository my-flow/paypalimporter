package com.moneydance.modules.features.paypalimporter.filter;

import java.util.function.Predicate;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

public final class NotAuthorizationFilter implements Predicate<PaymentTransactionSearchResultType> {

    private final Predicate<PaymentTransactionSearchResultType> authorizationFilter;

    public NotAuthorizationFilter() {
        this.authorizationFilter = new AuthorizationFilter();
    }

    @Override
    public boolean test(final PaymentTransactionSearchResultType result) {
        return Predicate.not(this.authorizationFilter).test(result);
    }
}
