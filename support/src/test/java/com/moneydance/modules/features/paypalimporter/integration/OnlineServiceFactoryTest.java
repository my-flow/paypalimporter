// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.infinitekind.moneydance.model.OnlineService;
import com.infinitekind.moneydance.model.StubOnlineInfo;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContext;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactoryTest {

    private static final String KEY_SERVICE_TYPE = "type";
    private OnlineServiceFactory onlineServiceFactory;
    private String serviceType;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.onlineServiceFactory = new OnlineServiceFactory(supportComponent.settings());
        this.serviceType = supportComponent.settings().getServiceType();
    }

    @Test
    public void testGetService() {
        IAccountBook accountBook = new StubContextFactory().getContext().getAccountBook();
        PayPalOnlineService service = this.onlineServiceFactory.createService(accountBook);
        assertThat(service, notNullValue());
        assertThat(this.onlineServiceFactory.createService(accountBook), not(service));
    }

    @Test
    public void testRemoveServiceEmpty() {
        IAccountBook accountBook = new StubContextFactory().getContext().getAccountBook();
        PayPalOnlineService service = this.onlineServiceFactory.createService(accountBook);
        assertThat(service, notNullValue());
        this.onlineServiceFactory.removeService(accountBook);
        assertThat(this.onlineServiceFactory.createService(accountBook), not(service));
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
                        OnlineServiceFactoryTest.this.serviceType);
            }
        });
        StubOnlineInfo onlineInfo = new StubOnlineInfo(
                context.getAccountBook(),
                Collections.singletonList(onlineService));
        IAccountBook accountBook = new StubAccountBook(
                context.getCurrentAccountBook(), onlineInfo);

        PayPalOnlineService service = this.onlineServiceFactory.createService(
                accountBook);
        assertThat(service, notNullValue());
        this.onlineServiceFactory.removeService(accountBook);
        assertThat(
                this.onlineServiceFactory.createService(accountBook),
                not(service));
    }
}
