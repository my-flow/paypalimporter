package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.apps.md.controller.FeatureModuleContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author Florian J. Breunig
 */
@Module(includes = {
        com.moneydance.modules.features.paypalimporter.bootstrap.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.controller.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.domain.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.model.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.service.FactoryModule.class,
        com.moneydance.modules.features.paypalimporter.util.FactoryModule.class,
})
final class CoreModule {

    private final FeatureModule featureModule;
    private FeatureModuleContext context;

    CoreModule(final FeatureModule argFeatureModule) {
        super();
        this.featureModule = argFeatureModule;
    }

    void setContext(final FeatureModuleContext argContext) {
        this.context = argContext;
    }

    @Provides
    FeatureModule provideFeatureModule() {
        return this.featureModule;
    }

    @Provides
    FeatureModuleContext provideFeatureModuleContext() {
        return this.context;
    }
}
