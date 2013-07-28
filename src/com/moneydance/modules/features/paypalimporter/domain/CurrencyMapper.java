// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import com.moneydance.apps.md.controller.Util;
import com.moneydance.apps.md.model.CurrencyTable;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.CurrencyUtil;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public enum CurrencyMapper {

    ; // no instances

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger
            .getLogger(CurrencyMapper.class.getName());

    public static CurrencyType getCurrencyTypeFromCurrencyCode(
            final CurrencyCodeType currencyCode,
            final CurrencyTable table) {
        Validate.notNull(currencyCode, "currency code must not be null");
        Validate.notNull(table, "currency table must not be null");

        final String name = currencyCode.getValue();
        CurrencyType currencyType = table.getCurrencyByIDString(name);
        if (currencyType != null) {
            return currencyType;
        }

        currencyType = CurrencyUtil.createDefaultTable(null)
                .getCurrencyByIDString(name);
        if (currencyType == null) {
            // no existing currency type matches, so create a new one
            currencyType = new CurrencyType(
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
            // Currency "bananas" is not supported by PayPal
            LOG.log(Level.WARNING,
                    String.format("PayPal does not support account currency %s",
                            currencyType.getIDString()),
                            e);
        }
        return result;
    }
}
