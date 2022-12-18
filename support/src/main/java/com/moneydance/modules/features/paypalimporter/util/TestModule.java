package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
            final IAccountBook accountBook,
            final FeatureModuleContext context,
            final Settings settings) {
        return new Preferences(
                context,
                accountBook,
                settings);
    }
}
