package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.StubContext;
import com.moneydance.apps.md.controller.StubContextFactory;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        com.moneydance.modules.features.paypalimporter.controller.MockModule.class,
        com.moneydance.modules.features.paypalimporter.domain.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.model.MockModule.class,
        com.moneydance.modules.features.paypalimporter.service.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.util.MockModule.class,
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
