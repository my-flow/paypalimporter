// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.OnlineService;
import com.infinitekind.moneydance.model.StubOnlineInfo;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContext;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.util.Helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactoryTest {

    private static final String KEY_SERVICE_TYPE = "type";

    @Test
    public void testConstructorIsPrivate()
            throws
            NoSuchMethodException,
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
        IAccountBook accountBook = new StubContextFactory().getContext().getAccountBook();
        PayPalOnlineService service = OnlineServiceFactory.createService(accountBook);
        assertThat(service, notNullValue());
        assertThat(OnlineServiceFactory.createService(accountBook), not(service));
    }

    @Test
    public void testRemoveServiceEmpty() {
        IAccountBook accountBook = new StubContextFactory().getContext().getAccountBook();
        PayPalOnlineService service = OnlineServiceFactory.createService(accountBook);
        assertThat(service, notNullValue());
        OnlineServiceFactory.removeService(accountBook);
        assertThat(OnlineServiceFactory.createService(accountBook), not(service));
    }

    @Test
    public void testRemoveExistingServices() {
        final StubContext context = new StubContextFactory().getContext();

        OnlineService onlineService = new OnlineService(
                context.getCurrentAccountBook());
        onlineService.addParameters(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                this.put(
                    KEY_SERVICE_TYPE,
                    Helper.INSTANCE.getSettings().getServiceType());
            }
        });
        StubOnlineInfo onlineInfo = new StubOnlineInfo(
                context.getAccountBook(),
                Collections.singletonList(onlineService));
        IAccountBook accountBook = new StubAccountBook(
                context.getCurrentAccountBook(), onlineInfo);

        PayPalOnlineService service = OnlineServiceFactory.createService(
                accountBook);
        assertThat(service, notNullValue());
        OnlineServiceFactory.removeService(accountBook);
        assertThat(
                OnlineServiceFactory.createService(accountBook),
                not(service));
    }
}
