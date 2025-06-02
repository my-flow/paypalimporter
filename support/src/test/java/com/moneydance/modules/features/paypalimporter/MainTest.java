package com.moneydance.modules.features.paypalimporter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.util.Settings;

public final class MainTest {

    private Settings settings;
    private Main main;

    @BeforeEach
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.settings = supportComponent.settings();

        this.main = new Main();
        final StubContextFactory factory = new StubContextFactory(this.main);
        factory.init();
        this.main.init();
    }

    @Test
    public void testInit() {
        this.main.init();
    }

    @Test
    public void testGetName() {
        assertThat(this.main.getName(), notNullValue());
    }

    @Test
    public void testGetIconImage() {
        assertThat(this.main.getIconImage(), notNullValue());
    }

    @Test
    public void testInvokeString() {
        this.main.invoke("");
        this.main.invoke(this.settings.getStartWizardSuffix());
    }
}
