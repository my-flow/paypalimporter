// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
import com.moneydance.modules.features.paypalimporter.model.AccountFilter;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
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
    ViewController provideViewController(
            final FeatureModuleContext context,
            final ServiceProvider serviceProvider,
            final DateConverter dateConverter,
            final IAccountBook accountBook,
            final AccountFilter accountFilter,
            final Settings settings,
            final Preferences prefs,
            final Localizable localizable) {
        return new ViewControllerImpl(
            context,
            serviceProvider,
            dateConverter,
            accountBook,
            accountFilter,
            settings,
            prefs,
            localizable);
    }
}
