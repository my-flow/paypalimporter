package com.moneydance.modules.features.paypalimporter.model;

import java.util.Objects;

/**
 * Immutable representation of a basic amount with currency information.
 * This class replaces the usage of PayPal SDK's BasicAmountType.
 */
public final class BasicAmount {

    private final String value;
    private final CurrencyCodeType currencyID;

    /**
     * Constructs a new BasicAmount with the specified value and currency.
     *
     * @param argValue the amount value
     * @param argCurrencyID the currency identifier
     */
    public BasicAmount(
            final String argValue,
            final CurrencyCodeType argCurrencyID) {
        this.value = argValue;
        this.currencyID = argCurrencyID;
    }

    /**
     * Gets the amount value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the currency identifier.
     *
     * @return the currency ID
     */
    public CurrencyCodeType getCurrencyID() {
        return this.currencyID;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BasicAmount that = (BasicAmount) obj;
        return Objects.equals(value, that.value)
                && Objects.equals(currencyID, that.currencyID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currencyID);
    }

    @Override
    public String toString() {
        return "BasicAmount{"
                + "value='" + value + '\''
                + ", currencyID=" + currencyID
                + '}';
    }
}
