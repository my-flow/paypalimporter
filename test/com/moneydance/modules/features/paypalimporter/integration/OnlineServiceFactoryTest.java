// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.apps.md.model.RootAccount;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactoryTest {

    @Test
    public void testConstructorIsPrivate()
            throws
            SecurityException,
            NoSuchMethodException,
            IllegalArgumentException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        Constructor<OnlineServiceFactory> constructor =
                OnlineServiceFactory.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

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
