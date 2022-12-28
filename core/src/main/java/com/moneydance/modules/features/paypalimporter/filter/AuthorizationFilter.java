package com.moneydance.modules.features.paypalimporter.filter;

import java.util.function.Predicate;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

final class AuthorizationFilter implements Predicate<PaymentTransactionSearchResultType> {

    private static final String TYPE = "Authorization";

    @Override
    public boolean test(final PaymentTransactionSearchResultType result) {
        return TYPE.equals(result.getType());
    }
}
