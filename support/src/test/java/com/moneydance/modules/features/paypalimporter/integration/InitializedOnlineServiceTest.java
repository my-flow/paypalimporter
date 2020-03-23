// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.
package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.util.StreamTable;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Florian J. Breunig
 */
public final class InitializedOnlineServiceTest {

    private AccountBook accountBook;
    private Settings settings;

    @Before
    public void setUp() {
        this.accountBook = new StubContextFactory().getContext().getAccountBook().getWrappedOriginal();

        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.settings = supportComponent.settings();
    }

    @Test
    public void testConstructorWithAccountBook() {
        final Date dateUpdated = new Date();
        final OnlineService onlineService = new InitializedOnlineService(
                this.accountBook,
                this.settings,
                dateUpdated);
        runTest(onlineService, dateUpdated);
    }

    @Test
    public void testConstructorWithAccountBookAndLegacyInfo() {
        final Date dateUpdated = new Date();
        final OnlineService onlineService = new InitializedOnlineService(
                this.accountBook,
                new StreamTable(),
                this.settings,
                dateUpdated);
        runTest(onlineService, dateUpdated);
    }

    private void runTest(final OnlineService onlineService, final Date dateUpdated) {
        assertThat(onlineService, notNullValue());
        assertThat(onlineService.getTIKServiceID(), notNullValue());
        assertThat(onlineService.getDateUpdated(), is(dateUpdated.getTime()));
    }
}
