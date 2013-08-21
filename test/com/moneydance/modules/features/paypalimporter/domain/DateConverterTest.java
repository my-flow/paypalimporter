// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Date;

import javax.swing.BoundedRangeModel;

import org.junit.Test;

public final class DateConverterTest {

    @Test
    public void testConstructorIsPrivate()
            throws
            SecurityException,
            NoSuchMethodException,
            IllegalArgumentException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        Constructor<DateConverter> constructor =
                DateConverter.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testGetBoundedRangeModel() {
        final Date startDate = new Date(Integer.MIN_VALUE);
        final Date valueDate = new Date(Integer.MAX_VALUE / 2);
        final Date endDate = new Date(Integer.MAX_VALUE);

        final BoundedRangeModel model = DateConverter.getBoundedRangeModel(
                startDate, endDate, valueDate);
        assertThat(model, notNullValue());
    }

    @Test
    public void testGetValidDate() {
        final Date validDate = DateConverter.getValidDate(new Date());
        assertThat(validDate, notNullValue());
    }

}
