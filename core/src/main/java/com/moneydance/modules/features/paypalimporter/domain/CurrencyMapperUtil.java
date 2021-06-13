// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.CurrencyUtil;
import com.moneydance.apps.md.controller.Util;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * This utility class contains domain-specific mappings between Moneydance's
 * <code>CurrencyType</code> and PayPal's <code>CurrencyCodeType</code>.
 *
 * @author Florian J. Breunig
 */
public final class CurrencyMapperUtil {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger
            .getLogger(CurrencyMapperUtil.class.getName());

    /**
     * Restrictive constructor.
     */
    private CurrencyMapperUtil() {
        // Prevents this class from being instantiated from the outside.
    }

    @SuppressWarnings("nullness")
    public static CurrencyType getCurrencyTypeFromCurrencyCode(
            final CurrencyCodeType currencyCode,
            final CurrencyTable table,
            final IAccountBook accountBook) {
        final String name = currencyCode.getValue();
        CurrencyType currencyType = table.getCurrencyByIDString(name);

        if (currencyType == null) {
            currencyType = CurrencyUtil.createDefaultTable(
                    accountBook.getWrappedOriginal(), null)
                    .getCurrencyByIDString(name);
            if (currencyType == null) {
                // no existing currency type matches, so create a new one
               currencyType = CurrencyType.currencyFromFields(
                     -1,
                     name,
                     name,
                     1.0D,
                     2,
                     name,
                     "",
                     name,
                     Util.convertDateToInt(
                             Calendar.getInstance().getTime()),
                             CurrencyType.CURRTYPE_CURRENCY,
                             table);
            }
            table.addCurrencyType(currencyType);
        }
        return currencyType;
    }

    public static CurrencyCodeType getCurrencyCodeFromCurrencyType(
            final CurrencyType currencyType,
            final List<CurrencyCodeType> availableCurrencies) {

        if (availableCurrencies.isEmpty()) {
            throw new IllegalArgumentException(
                    "No PayPal currencies available");
        }
        CurrencyCodeType result = availableCurrencies.get(0);
        try {
            final CurrencyCodeType accountCurrency = CurrencyCodeType.fromValue(
                    currencyType.getIDString());
            if (availableCurrencies.contains(accountCurrency)) {
                result = accountCurrency;
            } else {
                LOG.info(String.format("Account currency %s differs from"
                        + " PayPal account currencies", accountCurrency));
            }
        } catch (IllegalArgumentException e) {
            // Currency "bananas" is not supported by PayPal ;-)
            LOG.log(Level.WARNING,
                    String.format("PayPal does not support account currency %s",
                            currencyType.getIDString()),
                            e);
        }
        return result;
    }
}
