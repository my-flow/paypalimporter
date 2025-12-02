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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        final com.infinitekind.moneydance.model.AccountBook currentAccountBook =
                this.factory.getContext().getCurrentAccountBook();
        assertNotNull(currentAccountBook, "AccountBook should not be null");
        CurrencyTable defaultTable = CurrencyUtil.createDefaultTable(
                currentAccountBook,
                CurrencyCodeType.fromValue("USD").getValue());
        assertNotNull(defaultTable, "Default currency table should not be null");
        CurrencyType currencyType = CurrencyMapperUtil.getCurrencyTypeFromCurrencyCode(
                CurrencyCodeType.fromValue("USD"),
                defaultTable, null
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
        final com.infinitekind.moneydance.model.Account rootAccount =
                this.factory.getContext().getRootAccount();
        assertNotNull(rootAccount, "Root account should not be null");
        CurrencyType currencyType = rootAccount.getCurrencyType();
        assertNotNull(currencyType, "Currency type should not be null");
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                currencyType,
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
        final com.infinitekind.moneydance.model.AccountBook currentAccountBook =
                this.factory.getContext().getCurrentAccountBook();
        assertNotNull(currentAccountBook, "AccountBook should not be null");
        CurrencyTable currencyTable = new CurrencyTable(currentAccountBook);
        assertNotNull(currencyTable, "Currency table should not be null");
        CurrencyType currencyType = new CurrencyType(currencyTable);
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
        final com.infinitekind.moneydance.model.Account rootAccount =
                this.factory.getContext().getRootAccount();
        assertNotNull(rootAccount, "Root account should not be null");
        CurrencyType currencyType = rootAccount.getCurrencyType();
        assertNotNull(currencyType, "Currency type should not be null");
        CurrencyCodeType currencyCodeType = CurrencyMapperUtil.getCurrencyCodeFromCurrencyType(
                currencyType,
                Collections.singletonList(CurrencyCodeType.fromValue("EUR")));
        assertThat(currencyCodeType, is(CurrencyCodeType.fromValue("EUR")));
    }
}
