package com.moneydance.modules.features.paypalimporter.bootstrap;

import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public final class FactoryModule {

    @Provides
    @Singleton
    MainHelper provideMainHelper(final Settings settings) {
        return new MainHelper(settings);
    }
}
