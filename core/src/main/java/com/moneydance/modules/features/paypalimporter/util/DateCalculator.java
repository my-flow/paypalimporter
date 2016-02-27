// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for date calculations.
 *
 * @author Florian J. Breunig
 */
public final class DateCalculator {

    /**
     * Restrictive constructor.
     */
    private DateCalculator() {
        // Prevents this class from being instantiated from the outside.
    }

    public static Date max(final Date date1, final Date date2) {
        if (date1.after(date2)) {
            return date1;
        }
        return date2;
    }

    /**
     * Get a diff between two dates.
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(
            final Date date1,
            final Date date2,
            final TimeUnit timeUnit) {
        final long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
