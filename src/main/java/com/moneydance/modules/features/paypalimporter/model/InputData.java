// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.DateRange;
import com.moneydance.apps.md.controller.Util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Data model of the user data. Effectively immutable.
 *
 * @author Florian J. Breunig
 */
public final class InputData {

    private final String username;
    private final char[] password;
    private final String signature;
    private final int accountId;
    private final int startDateInt;
    private final int endDateInt;

    public InputData(
            final String argUsername,
            final char[] argPassword,
            final String argSignature,
            final int argAccountId) {

        this(argUsername, argPassword, argSignature, argAccountId, null);
    }

    public InputData(
            final String argUsername,
            final char[] argPassword,
            final String argSignature,
            final int argAccountId,
            final DateRange argDateRange) {

        this.username = argUsername;
        if (argPassword == null) {
            this.password = argPassword;
        } else {
            this.password = Arrays.copyOf(argPassword, argPassword.length);
        }
        this.signature = argSignature;
        this.accountId = argAccountId;
        if (argDateRange == null) {
            this.startDateInt = -1;
            this.endDateInt = -1;
        } else {
            this.startDateInt = argDateRange.getStartDateInt();
            this.endDateInt = argDateRange.getEndDateInt();
        }
    }

    public String getUsername() {
        return this.username;
    }

    public char[] getPassword(final boolean clear) {
        char[] result;
        if (this.password == null) {
            result = null;
        } else {
            result = Arrays.copyOf(this.password, this.password.length);
            if (clear) {
                Arrays.fill(this.password, '\0');
            }
        }
        return result;
    }

    public String getSignature() {
        return this.signature;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public Date getStartDate() {
        return DateUtils.truncate(
                Util.convertIntDateToLong(this.startDateInt),
                Calendar.DATE);
    }

    public Date getEndDate() {
        return DateUtils.ceiling(
                Util.convertIntDateToLong(this.endDateInt),
                Calendar.DATE);
    }

    public DateRange getDateRange() {
        if (this.startDateInt >= 0 && this.endDateInt >= 0) {
            return new DateRange(this.startDateInt, this.endDateInt);
        }
        return null;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(
                this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
