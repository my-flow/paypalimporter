// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

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
    DateConverter provideDateConverter(final Settings settings) {
        return new DateConverter(settings.getMinDate());
    }
}
