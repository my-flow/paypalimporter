// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

/**
 * @author Florian J. Breunig
 * @param <V> The type of result objects which a service returns.
 */
@Immutable
public final class ServiceResult<V> {

    private final List<V> results;
    private final String errorCode;
    private final String errorMessage;

    ServiceResult(
            final List<V> argResults,
            final String argErrorCode,
            final String argErrorMessage) {
        if (argResults == null) {
            this.results = null;
        } else {
            this.results = Collections.unmodifiableList(
                    new ArrayList<V>(argResults));
        }
        this.errorCode = argErrorCode;
        this.errorMessage = argErrorMessage;
    }

    ServiceResult(final String argErrorMessage) {
        this.results = null;
        this.errorCode = null;
        this.errorMessage = argErrorMessage;
    }

    public List<V> getResults() {
        return this.results;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
