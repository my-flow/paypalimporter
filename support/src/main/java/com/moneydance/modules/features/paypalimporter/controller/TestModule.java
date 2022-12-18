package com.moneydance.modules.features.paypalimporter.controller;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class TestModule {

    @Provides
    @Singleton
    ViewController provideViewController() {
        return new ViewControllerMock();
    }
}
