// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Florian J. Breunig
 */
@Module
public final class FactoryModule {

    @Provides
    @Singleton
    ServiceProvider provideServiceProvider(
            final DateConverter dateConverter,
            final Settings settings,
            final Preferences prefs,
            final Localizable localizable) {
        return new ServiceProviderImpl(
                dateConverter,
                settings,
                prefs,
                localizable);
    }
}
