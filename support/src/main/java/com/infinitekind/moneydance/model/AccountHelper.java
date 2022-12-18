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
