package com.moneydance.modules.features.paypalimporter.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;
import com.moneydance.modules.features.paypalimporter.bootstrap.Helper;

public final class HelperTest {

    private Settings settings;

    @BeforeEach
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.settings = supportComponent.settings();
    }

    @Test
    public void testGetInputStreamFromResource() {
        assertThat(Helper.getInputStreamFromResource(
                this.settings.getLoggingPropertiesResource()),
                notNullValue());
    }
}
