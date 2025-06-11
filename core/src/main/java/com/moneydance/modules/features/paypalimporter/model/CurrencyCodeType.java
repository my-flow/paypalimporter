package com.moneydance.modules.features.paypalimporter.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enumeration of currency codes supported by PayPal.
 * This enum replaces the usage of PayPal SDK's CurrencyCodeType.
 */
public final class CurrencyCodeType {

    /**
     * Thread-safe map to store instances
     */
    private static final Map<String, CurrencyCodeType> INSTANCES = new ConcurrentHashMap<>();

    private final String value;

    public static final CurrencyCodeType UNKNOWN = fromValue("UNKNOWN");

    /**
     * Private constructor to prevent direct instantiation.
     *
     * @param argValue the string representation of the currency code
     */
    private CurrencyCodeType(final String argValue) {
        this.value = argValue;
    }

    /**
     * Gets the string value of this currency code.
     *
     * @return the currency code value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the CurrencyCodeType instance with the specified value.
     * If an instance with the given value doesn't exist, it creates and returns a new one.
     * This method is thread-safe and ensures only one instance per currency code exists.
     *
     * @param argValue the string value to match (case-sensitive)
     * @return the corresponding CurrencyCodeType instance
     * @throws IllegalArgumentException if argValue is null or empty
     */
    public static CurrencyCodeType fromValue(final String argValue) {
        if (StringUtils.isBlank(argValue)) {
            throw new IllegalArgumentException("Currency code value cannot be null or empty");
        }

        // Use computeIfAbsent for thread-safe lazy initialization
        return INSTANCES.computeIfAbsent(argValue.trim().toUpperCase(Locale.ROOT), CurrencyCodeType::new);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CurrencyCodeType that = (CurrencyCodeType) obj;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
