// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import javax.annotation.Nullable;

/**
 * @author Florian J. Breunig
 * @param <V> The type of result objects which a service returns.
 */
@Immutable
public final class ServiceResult<V> {

    @Nullable private final List<V> results;
    @Nullable private final String errorCode;
    @Nullable private final String errorMessage;

    ServiceResult(
            @Nullable final List<V> argResults,
            @Nullable final String argErrorCode,
            @Nullable final String argErrorMessage) {
        if (argResults == null) {
            this.results = null;
        } else {
            this.results = Collections.unmodifiableList(
                    new ArrayList<V>(argResults));
        }
        this.errorCode = argErrorCode;
        this.errorMessage = argErrorMessage;
    }

    ServiceResult(@Nullable final String argErrorMessage) {
        this.results = null;
        this.errorCode = null;
        this.errorMessage = argErrorMessage;
    }

    @Nullable public List<V> getResults() {
        return this.results;
    }

    @Nullable public String getErrorCode() {
        return this.errorCode;
    }

    @Nullable public String getErrorMessage() {
        return this.errorMessage;
    }
}
