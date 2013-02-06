/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.model;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.moneydance.apps.md.controller.DateRange;

/**
 * @author Florian J. Breunig
 */
public final class MutableInputData implements InputData {

    private String username;
    private char[] password;
    private String signature;
    private int accountId;
    private DateRange dateRange;

    public MutableInputData() {
    }

    public MutableInputData(
            final String argUsername,
            final char[] argPassword,
            final String argSignature,
            final int argAccountId) {

        this.fill(argUsername, argPassword, argSignature, argAccountId, null);
    }

    public void fill(
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
        this.dateRange = argDateRange;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public char[] getPassword(final boolean clear) {
        if (this.password == null) {
            return null;
        }
        final char[] ret = Arrays.copyOf(this.password, this.password.length);
        if (clear) {
            Arrays.fill(this.password, '\0');
        }
        return ret;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

    @Override
    public int getAccountId() {
        return this.accountId;
    }

    @Override
    public DateRange getDateRange() {
        return this.dateRange;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(
                this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
