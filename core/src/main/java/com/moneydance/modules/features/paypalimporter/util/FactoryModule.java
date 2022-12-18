package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.Main;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.apache.commons.configuration.ConfigurationException;

/**
 * @author Florian J. Breunig
 */
@Module
public final class FactoryModule {

    private static final String SETTINGS_RESOURCE = "settings.properties";

    @Provides
    @Singleton
    Settings provideSettings() {
        try {
            return new Settings(SETTINGS_RESOURCE);
        } catch (IOException | ConfigurationException | ParseException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Provides
    @Singleton
    Localizable provideLocalizable(final Settings settings, final Locale locale) {
        return new Localizable(settings.getLocalizableResource(), locale);
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

    @Provides
    Locale provideLocale(final FeatureModuleContext argContext) {
        final UserPreferences userPreferences = ((Main) argContext).getPreferences();
        return userPreferences.getLocale();
    }
}
