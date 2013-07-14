// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class MutableInputDataTest {

    @Test
    public void testGetUsername() {
        InputData inputData = new MutableInputData();
        assertThat(inputData.getUsername(), nullValue());
    }

    @Test
    public void testGetPassword() {
        final InputData inputData1 = new MutableInputData(null, null, null, -1);
        assertThat(inputData1.getPassword(false), nullValue());

        final char[] password = {'s', 't', 'u', 'b', ' ',
                'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        final InputData inputData2 = new MutableInputData(null, password, null, -1);
        assertThat(password, is(inputData2.getPassword(true)));
        assertThat(password, is(not(inputData2.getPassword(false))));
    }

    @Test
    public void testGetSignature() {
        InputData inputData = new MutableInputData();
        assertThat(inputData.getSignature(), nullValue());
    }

    @Test
    public void testGetAccountId() {
        final int accountID = 3;
        InputData inputData = new MutableInputData(null, null, null, accountID);
        assertThat(inputData.getAccountId(), is(accountID));
    }

    @Test
    public void testGetDateRange() {
        InputData inputData = new MutableInputData();
        assertThat(inputData.getDateRange(), nullValue());
    }

    @Test
    public void testToString() {
        InputData inputData = new MutableInputData();
        assertThat(inputData.toString(), notNullValue());
    }
}
