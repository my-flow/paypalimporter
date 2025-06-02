package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public final class DateCalculatorUtilTest {

    @Test
    public void testConstructorIsPrivate()
            throws NoSuchMethodException {

        Constructor<DateCalculatorUtil> constructor =
                DateCalculatorUtil.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }
}
