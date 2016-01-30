// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class DateCalculatorTest {

    @Test
    public void testConstructorIsPrivate()
            throws
            SecurityException,
            NoSuchMethodException,
            IllegalArgumentException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        Constructor<DateCalculator> constructor =
                DateCalculator.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
