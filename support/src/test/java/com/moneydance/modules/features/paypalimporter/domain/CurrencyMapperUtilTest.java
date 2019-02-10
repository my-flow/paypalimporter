// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.CurrencyUtil;
import com.moneydance.apps.md.controller.StubContextFactory;

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
public final class CurrencyMapperUtilTest {

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

        Constructor<CurrencyMapperUtil> constructor =
                CurrencyMapperUtil.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithDefaultTable() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.USD,
                CurrencyUtil.createDefaultTable(
                        this.factory.getContext().getCurrentAccountBook(),
                        CurrencyCodeType.USD.getValue()), null
                );
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.USD.getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithEmptyCurrencyTable() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.USD,
                this.factory.getContext().getAccountBook().getCurrencies(),
                this.factory.getContext().getAccountBook());
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.USD.getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithUnknownCurrencyCode() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.NIO,
                this.factory.getContext().getAccountBook().getCurrencies(),
                this.factory.getContext().getAccountBook()
        );
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.NIO.getValue()));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithSingleCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.USD));
        assertThat(currencyCodeType, is(CurrencyCodeType.USD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCurrencyCodeFromCurrencyTypeWhenEmpty() {
        CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getCurrentAccountBook().getRootAccount().getCurrencyType(),
                Collections.<CurrencyCodeType>emptyList());
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWhenUnknown() {
        CurrencyType currencyType = new CurrencyType(
                new CurrencyTable(this.factory.getContext().getCurrentAccountBook())
        );
        currencyType.setCurrencyType(CurrencyType.Type.CURRENCY);
        currencyType.setName("Banana");
        currencyType.setIDString("BAN");

        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                currencyType,
                Collections.singletonList(CurrencyCodeType.USD));
        assertThat(currencyCodeType, is(CurrencyCodeType.USD));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithUnsupportedCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.EUR));
        assertThat(currencyCodeType, is(CurrencyCodeType.EUR));
    }
}
