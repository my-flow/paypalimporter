// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.modules.features.paypalimporter.bootstrap.MainHelper;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Florian J. Breunig
 */
@Component(modules = {
        CoreModule.class,
})
@Singleton
public interface CoreComponent {

    Settings settings();

    Preferences preferences();

    Localizable localizable();

    FeatureModuleContext context();

    MainHelper mainHelper();

    ViewController viewController();
}
