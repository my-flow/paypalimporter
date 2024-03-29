package com.moneydance.modules.features.paypalimporter.model;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContext;

import org.junit.Test;

import java.util.Optional;

public final class AccountBookFactoryImplTest {

    @Test
    public void testCreateAccountBook() {
        IAccountBookFactory factory = AccountBookFactoryImpl.INSTANCE;
        StubContext context = new StubContext(null, new StubAccountBook(null));
        assertThat(factory.createAccountBook(context), is(Optional.empty()));
    }
}
