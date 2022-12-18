package com.moneydance.modules.features.paypalimporter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.moneydance.apps.md.controller.StubContextFactory;
import com.moneydance.modules.features.paypalimporter.util.Settings;

/**
 * @author Florian J. Breunig
 */
public final class MainTest {

    private Settings settings;
    private Main main;

    @Before
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
