// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import com.moneydance.modules.features.paypalimporter.util.DateCalculator;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;

import org.apache.commons.lang3.Validate;

/**
 * This utility class contains domain-specific date conversions.
 *
 * @author Florian J. Breunig
 */
public final class DateConverter {

    /**
     * Restrictive constructor.
     */
    private DateConverter() {
        // Prevents this class from being instantiated from the outside.
    }

    public static BoundedRangeModel getBoundedRangeModel(
            final Date startDate,
            final Date endDate,
            final Date valueDate) {

        Validate.notNull(startDate, "start date must not be null");
        Validate.notNull(endDate,   "end date must not be null");
        Validate.notNull(valueDate, "value date must not be null");
        Validate.inclusiveBetween(
                startDate,
                endDate,
                valueDate);

        final TimeUnit timeUnit = TimeUnit.SECONDS;
        final int value = safeLongToInt(DateCalculator.getDateDiff(
                getValidDate(valueDate),
                getValidDate(endDate),
                timeUnit));
        final int max = safeLongToInt(DateCalculator.getDateDiff(
                getValidDate(startDate),
                getValidDate(endDate),
                timeUnit));
        return new DefaultBoundedRangeModel(
                value,
                0,
                0,
                max);
    }

    public static Date getValidDate(final Date date) {
        return DateCalculator.max(
                date,
                Helper.INSTANCE.getSettings().getMinDate());
    }

    private static int safeLongToInt(final long longValue) {
        if (longValue < Integer.MIN_VALUE || longValue > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "%d cannot be cast to int without changing its value.",
                    longValue));
        }
        return (int) longValue;
    }
}
