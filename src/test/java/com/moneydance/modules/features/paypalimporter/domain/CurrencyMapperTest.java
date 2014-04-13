// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.model.CurrencyType;
import com.moneydance.apps.md.model.CurrencyUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;

/**
 * @author Florian J. Breunig
 */
public final class CurrencyMapperTest {

    private StubContextFactory factory;

    @Before
    public void setUp() {
        this.factory = new StubContextFactory();
    }

    @Test
    public void testConstructorIsPrivate()
            throws
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        Constructor<CurrencyMapper> constructor =
                CurrencyMapper.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithDefaultTable() {
        CurrencyType currencyType = CurrencyMapper.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.USD,
                CurrencyUtil.createDefaultTable(CurrencyCodeType.USD.getValue()));
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.USD.getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithEmptyCurrencyTable() {
        CurrencyType currencyType = CurrencyMapper.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.USD,
                this.factory.getContext().getRootAccount().getCurrencyTable());
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.USD.getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithUnknownCurrencyCode() {
        CurrencyType currencyType = CurrencyMapper.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.NIO,
                this.factory.getContext().getRootAccount().getCurrencyTable());
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.NIO.getValue()));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithSingleCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapper.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.USD));
        assertThat(currencyCodeType, is(CurrencyCodeType.USD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCurrencyCodeFromCurrencyTypeWhenEmpty() {
        CurrencyMapper.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyTable().getBaseType(),
                Collections.<CurrencyCodeType>emptyList());
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWhenUnknown() {
        CurrencyType currencyType = new CurrencyType(
                -1,
                "Banana",
                "Banana",
                1.0D,
                0,
                "B",
                "",
                "BAN",
                CurrencyType.CURRTYPE_CURRENCY,
                0,
                null);

        CurrencyCodeType currencyCodeType = CurrencyMapper.getCurrencyCodeFromCurrencyType(
                currencyType,
                Collections.singletonList(CurrencyCodeType.USD));
        assertThat(currencyCodeType, is(CurrencyCodeType.USD));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithUnsupportedCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapper.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.EUR));
        assertThat(currencyCodeType, is(CurrencyCodeType.EUR));
    }
}
