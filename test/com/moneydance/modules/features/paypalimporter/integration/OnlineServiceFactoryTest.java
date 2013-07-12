/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.model.RootAccount;

/**
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactoryTest {

    @Test
    public void testGetService() {
        RootAccount rootAccount = new StubContextFactory().getContext().getRootAccount();
        PayPalOnlineService service = OnlineServiceFactory.getService(rootAccount);
        assertThat(service, notNullValue());
        assertThat(OnlineServiceFactory.getService(rootAccount), not(service));
    }

    @Test
    public void testRemoveService() {
        RootAccount rootAccount = new StubContextFactory().getContext().getRootAccount();
        PayPalOnlineService service = OnlineServiceFactory.getService(rootAccount);
        assertThat(service, notNullValue());
        OnlineServiceFactory.removeService(rootAccount);
        assertThat(OnlineServiceFactory.getService(rootAccount), not(service));

        rootAccount = new StubContextFactory().addOnlineService().getContext().getRootAccount();
        service = OnlineServiceFactory.getService(rootAccount);
        assertThat(service, notNullValue());
        OnlineServiceFactory.removeService(rootAccount);
        assertThat(OnlineServiceFactory.getService(rootAccount), not(service));
    }
}
