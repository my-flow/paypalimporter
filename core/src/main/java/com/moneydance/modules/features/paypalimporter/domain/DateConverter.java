package com.moneydance.modules.features.paypalimporter.domain;

import com.moneydance.modules.features.paypalimporter.util.DateCalculatorUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;

import org.apache.commons.lang3.Validate;

/**
 * This utility class contains domain-specific date conversions.
 */
public final class DateConverter {

    private final Date minDate;

    DateConverter(final Date argMinDate) {
        this.minDate = argMinDate;
    }

    public BoundedRangeModel getBoundedRangeModel(
            final Date startDate,
            final Date endDate,
            final Date valueDate) {

        Validate.inclusiveBetween(
                startDate,
                endDate,
                valueDate);

        final TimeUnit timeUnit = TimeUnit.SECONDS;
        final int value = safeLongToInt(DateCalculatorUtil.getDateDiff(
                this.getValidDate(valueDate),
                this.getValidDate(endDate),
                timeUnit));
        final int max = safeLongToInt(DateCalculatorUtil.getDateDiff(
                this.getValidDate(startDate),
                this.getValidDate(endDate),
                timeUnit));
        return new DefaultBoundedRangeModel(
                value,
                0,
                0,
                max);
    }

    public Date getValidDate(final Date date) {
        return DateCalculatorUtil.max(
                date,
                this.minDate);
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
