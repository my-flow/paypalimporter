// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.infinitekind.moneydance.model;

/**
 * @author Florian J. Breunig
 */
public final class AccountHelper {

    /**
     * Restrictive constructor.
     */
    private AccountHelper() {
        // Prevents this class from being instantiated from the outside.
    }

    public static void addSubAccount(
            final Account parent,
            final Account subaccount
            ) {
        parent.ensureHasSubAccount(subaccount);
    }
}
