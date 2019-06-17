// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.StubAccountBookFactory;
import com.moneydance.apps.md.controller.StubContextFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Florian J. Breunig
 */
@Module
public final class TestModule {

    private final FactoryModule factoryModule = new FactoryModule();

    @Provides
    @Singleton
    Settings provideSettings() {
        return this.factoryModule.provideSettings();
    }

    @Provides
    @Singleton
    Localizable provideLocalizable(final Settings settings, final Preferences prefs) {
        return this.factoryModule.provideLocalizable(settings, prefs.getLocale());
    }

    @Provides
    @Singleton
    Preferences providePreferences(
            final FeatureModuleContext context,
            final Settings settings) {

        final StubContextFactory factory = new StubContextFactory();
        return new Preferences(
                context,
                new StubAccountBookFactory(
                        factory.getContext().getAccountBook()),
                settings);
    }
}
