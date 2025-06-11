package com.moneydance.modules.features.paypalimporter.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.infinitekind.moneydance.model.CurrencyTable;
import com.infinitekind.moneydance.model.CurrencyType;
import com.infinitekind.moneydance.model.CurrencyUtil;
import com.moneydance.apps.md.controller.StubContextFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.moneydance.modules.features.paypalimporter.model.CurrencyCodeType;

public final class CurrencyMapperUtilTest {

    private StubContextFactory factory;

    @BeforeEach
    public void setUp() {
        this.factory = new StubContextFactory();
    }

    @Test
    public void testConstructorIsPrivate()
            throws NoSuchMethodException {

        Constructor<CurrencyMapperUtil> constructor =
                CurrencyMapperUtil.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithDefaultTable() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.fromValue("USD"),
                CurrencyUtil.createDefaultTable(
                        this.factory.getContext().getCurrentAccountBook(),
                        CurrencyCodeType.fromValue("USD").getValue()), null
                );
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.fromValue("USD").getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithEmptyCurrencyTable() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.fromValue("USD"),
                this.factory.getContext().getAccountBook().getCurrencies(),
                this.factory.getContext().getAccountBook());
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.fromValue("USD").getValue()));
    }

    @Test
    public void testGetCurrencyTypeFromCurrencyCodeWithUnknownCurrencyCode() {
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.fromValue("MYR"),
                this.factory.getContext().getAccountBook().getCurrencies(),
                this.factory.getContext().getAccountBook()
        );
        assertThat(currencyType.getIDString(), is(CurrencyCodeType.fromValue("MYR").getValue()));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithSingleCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.fromValue("USD")));
        assertThat(currencyCodeType, is(CurrencyCodeType.fromValue("USD")));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWhenEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                        this.factory.getContext().getCurrentAccountBook().getRootAccount().getCurrencyType(),
                        Collections.<CurrencyCodeType>emptyList()));
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
                Collections.singletonList(CurrencyCodeType.fromValue("USD")));
        assertThat(currencyCodeType, is(CurrencyCodeType.fromValue("USD")));
    }

    @Test
    public void testGetCurrencyCodeFromCurrencyTypeWithUnsupportedCurrency() {
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                this.factory.getContext().getRootAccount().getCurrencyType(),
                Collections.singletonList(CurrencyCodeType.fromValue("EUR")));
        assertThat(currencyCodeType, is(CurrencyCodeType.fromValue("EUR")));
    }
}
