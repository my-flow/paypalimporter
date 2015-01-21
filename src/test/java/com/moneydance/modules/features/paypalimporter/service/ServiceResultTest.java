// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class ServiceResultTest {

    @Test
    public void testGetResults() {
        List<String> results = Collections.<String>emptyList();
        ServiceResult<String> serviceResult = new ServiceResult<String>(
                results, null, null);
        assertThat(serviceResult.getResults(), is(results));
        assertThat(serviceResult.getErrorCode(), nullValue());
        assertThat(serviceResult.getErrorMessage(), nullValue());
    }

    @Test
    public void testGetErrorCode() {
        String errorCode = "stub error code";
        ServiceResult<String> serviceResult = new ServiceResult<String>(
                null, errorCode, null);
        assertThat(serviceResult.getResults(), nullValue());
        assertThat(serviceResult.getErrorCode(), is(errorCode));
        assertThat(serviceResult.getErrorMessage(), nullValue());
    }

    @Test
    public void testGetErrorMessage() {
        String errorMessage = "stub error message";
        ServiceResult<String> serviceResult = new ServiceResult<String>(
                null, null, errorMessage);
        assertThat(serviceResult.getResults(), nullValue());
        assertThat(serviceResult.getErrorCode(), nullValue());
        assertThat(serviceResult.getErrorMessage(), is(errorMessage));

        // test convenience constructor
        serviceResult = new ServiceResult<String>(errorMessage);
        assertThat(serviceResult.getResults(), nullValue());
        assertThat(serviceResult.getErrorCode(), nullValue());
        assertThat(serviceResult.getErrorMessage(), is(errorMessage));
    }
}
