// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.apps.md.controller.DateRange;
import com.moneydance.apps.md.controller.StubContextFactory;

/**
 * @author Florian J. Breunig
 */
final public class InputDataValidatorTest {

    private Validator<InputData> validator;

    @Before
    public void setUp() throws Exception {
        new StubContextFactory();
        this.validator = new InputDataValidator();
    }

    @Test
    public void testValidateAllEmpty() {
        InputData inputData = new MutableInputData();
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.USERNAME),  is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.PASSWORD),  is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.SIGNATURE), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.DATERANGE), is(true));
    }

    @Test
    public void testValidateUsername() {
        final String username  = "mock username";
        InputData inputData = new MutableInputData(username, null, null, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.USERNAME), is(false));
    }

    @Test
    public void testValidatePassword() {
        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        InputData inputData = new MutableInputData(null, password, null, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.PASSWORD), is(false));
    }

    @Test
    public void testValidateSignature() {
        final String signature  = "mock signature";
        InputData inputData = new MutableInputData(null, null, signature, -1);
        ValidationResult result = this.validator.validate(inputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.SIGNATURE), is(false));
    }

    @Test
    public void testValidateDateRange() {
        final DateRange dateRange = new DateRange(0,  1);
        MutableInputData mutableInputData = new MutableInputData();
        mutableInputData.fill(null, null, null, -1, dateRange);
        ValidationResult result = this.validator.validate(mutableInputData);
        assertThat(result.hasErrors(), is(true));
        assertThat(result.keyMap().containsKey(InputDataValidator.MessageKey.DATERANGE), is(false));
    }
}
