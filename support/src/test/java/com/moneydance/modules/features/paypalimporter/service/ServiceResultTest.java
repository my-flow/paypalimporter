// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class ServiceResultTest {

    @Test
    public void testGetResults() {
        List<String> results = Collections.emptyList();
        ServiceResult<String> serviceResult = new ServiceResult<>(
                results, null, null);
        assertThat(serviceResult.getResults().orElseThrow(AssertionError::new), is(results));
        assertThat(serviceResult.getErrorCode(), is(Optional.empty()));
        assertThat(serviceResult.getErrorMessage(), is(Optional.empty()));
    }

    @Test
    public void testGetErrorCode() {
        String errorCode = "stub error code";
        ServiceResult<String> serviceResult = new ServiceResult<>(
                null, errorCode, null);
        assertThat(serviceResult.getResults(), is(Optional.empty()));
        assertThat(serviceResult.getErrorCode().orElseThrow(AssertionError::new), is(errorCode));
        assertThat(serviceResult.getErrorMessage(), is(Optional.empty()));
    }

    @Test
    public void testGetErrorMessage() {
        String errorMessage = "stub error message";
        ServiceResult<String> serviceResult = new ServiceResult<>(
                null, null, errorMessage);
        assertThat(serviceResult.getResults(), is(Optional.empty()));
        assertThat(serviceResult.getErrorCode(), is(Optional.empty()));
        assertThat(serviceResult.getErrorMessage().orElseThrow(AssertionError::new), is(errorMessage));
    }

    @Test
    public void testGetErrorMessageWithConvenienceConstructor() {
        String errorMessage = "stub error message";
        ServiceResult<String> serviceResult = new ServiceResult<>(errorMessage);
        assertThat(serviceResult.getResults(), is(Optional.empty()));
        assertThat(serviceResult.getErrorCode(), is(Optional.empty()));
        assertThat(serviceResult.getErrorMessage().orElseThrow(AssertionError::new), is(errorMessage));
    }
}
