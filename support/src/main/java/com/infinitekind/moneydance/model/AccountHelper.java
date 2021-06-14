// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

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
