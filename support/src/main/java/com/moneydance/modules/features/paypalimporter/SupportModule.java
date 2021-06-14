// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.StubContext;
import com.moneydance.apps.md.controller.StubContextFactory;

import dagger.Module;
import dagger.Provides;

/**
 * @author Florian J. Breunig
 */
@Module(includes = {
        com.moneydance.modules.features.paypalimporter.controller.TestModule.class,
        com.moneydance.modules.features.paypalimporter.domain.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.model.TestModule.class,
        com.moneydance.modules.features.paypalimporter.service.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.util.TestModule.class,
})
public final class SupportModule {

    private final StubContext context;

    public SupportModule() {
        super();
        StubContextFactory factory = new StubContextFactory();
        this.context = factory.getContext();
    }

    @Provides
    FeatureModuleContext provideFeatureModuleContext() {
        return this.context;
    }
}
