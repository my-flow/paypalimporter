// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.infinitekind.moneydance.model.DateRange;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.apps.md.controller.StubContextFactory;

/**
 * @author Florian J. Breunig
 */
public final class InputDataValidatorTest {

    private Validator<InputData> validator;

    @Before
    public void setUp() {
        new StubContextFactory();
        this.validator = new InputDataValidator();
    }

    @Test
    public void testValidateUsername() {
        final String username  = "mock username";
        InputData inputData = new InputData(username, null, null, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.USERNAME), is(false));
    }

    @Test
    public void testValidatePassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        InputData inputData = new InputData(null, password, null, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.PASSWORD), is(false));
    }

    @Test
    public void testValidateSignature() {
        final String signature  = "mock signature";
        InputData inputData = new InputData(null, null, signature, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.SIGNATURE), is(false));
    }

    @Test
    public void testValidateDateRange() {
        final DateRange dateRange = new DateRange(0,  1);
        InputData inputData = new InputData(null, null, null, -1, dateRange);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.DATERANGE), is(false));
    }
}
