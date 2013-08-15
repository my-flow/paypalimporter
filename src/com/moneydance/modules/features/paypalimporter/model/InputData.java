// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.DateRange;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Florian J. Breunig
 */
public final class InputData {

    private final String username;
    private final char[] password;
    private final String signature;
    private final int accountId;
    private final int startDate;
    private final int endDate;

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
            this.startDate = -1;
            this.endDate = -1;
        } else {
            this.startDate = argDateRange.getStartDateInt();
            this.endDate = argDateRange.getEndDateInt();
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

    public DateRange getDateRange() {
        if (this.startDate >= 0 && this.endDate >= 0) {
            return new DateRange(this.startDate, this.endDate);
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
