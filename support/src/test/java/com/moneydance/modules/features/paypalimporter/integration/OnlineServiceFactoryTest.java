package com.moneydance.modules.features.paypalimporter.integration;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContext;
import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class OnlineServiceFactoryTest {

    private static final String KEY_SERVICE_TYPE = "type";
    private OnlineServiceFactory onlineServiceFactory;
    private String serviceType;

    @BeforeEach
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

        final com.infinitekind.moneydance.model.AccountBook currentAccountBook =
                context.getCurrentAccountBook();
        assertNotNull(currentAccountBook, "AccountBook should not be null");
        OnlineService onlineService = new OnlineService(currentAccountBook);
        onlineService.addParameters(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                this.put(
                    KEY_SERVICE_TYPE,
                        OnlineServiceFactoryTest.this.serviceType);
            }
        });
        OnlineInfo onlineInfo = new OnlineInfo(currentAccountBook);
        IAccountBook accountBook = new StubAccountBook(currentAccountBook, onlineInfo);

        PayPalOnlineService service = this.onlineServiceFactory.createService(
                accountBook);
        assertThat(service, notNullValue());
        this.onlineServiceFactory.removeService(accountBook);
        assertThat(
                this.onlineServiceFactory.createService(accountBook),
                not(service));
    }
}
