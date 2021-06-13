// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class DateCalculatorUtilTest {

    @Test
    public void testConstructorIsPrivate()
            throws NoSuchMethodException {

        Constructor<DateCalculatorUtil> constructor =
                DateCalculatorUtil.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }
}
