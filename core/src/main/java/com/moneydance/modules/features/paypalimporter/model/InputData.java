// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.DateRange;
import com.moneydance.apps.md.controller.Util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateUtils;

import javax.annotation.Nullable;

/**
 * Data model of the user data. Effectively immutable.
 *
 * @author Florian J. Breunig
 */
public final class InputData {

    @Nullable private final String username;
    @SuppressWarnings("nullness") private final char[] password;
    @Nullable private final String signature;
    private final String accountId;
    private final int startDateInt;
    private final int endDateInt;

    public InputData() {
        this(null,  null,  null,  null);
    }

    @SuppressWarnings("nullness")
    public InputData(
            @Nullable final String argUsername,
            @Nullable final char[] argPassword,
            @Nullable final String argSignature,
            final String argAccountId) {

        this(argUsername, argPassword, argSignature, argAccountId, null);
    }

    @SuppressWarnings("nullness")
    public InputData(
            @Nullable final String argUsername,
            @Nullable final char[] argPassword,
            @Nullable final String argSignature,
            final String argAccountId,
            @Nullable final DateRange argDateRange) {

        this.username = argUsername;
        if (argPassword == null) {
            this.password = null;
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

    public Optional<String> getUsername() {
        return Optional.ofNullable(this.username);
    }

    public Optional<char[]> getPassword(final boolean clear) {
        Optional<char[]> result;
        if (this.password == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(Arrays.copyOf(this.password, this.password.length));
            if (clear) {
                Arrays.fill(this.password, '\0');
            }
        }
        return result;
    }

    public Optional<String> getSignature() {
        return Optional.ofNullable(this.signature);
    }

    public Optional<String> getAccountId() {
        return Optional.ofNullable(this.accountId);
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

    public Optional<DateRange> getDateRange() {
        if (this.startDateInt >= 0 && this.endDateInt >= 0) {
            return Optional.of(new DateRange(this.startDateInt, this.endDateInt));
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(
                this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
