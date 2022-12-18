package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class FactoryModule {

    @Provides
    @Singleton
    IAccountBookFactory provideAccountBookFactory() {
        return AccountBookFactoryImpl.INSTANCE;
    }

    @Provides
    @Singleton
    IAccountBook provideAccountBook(
            final IAccountBookFactory accountBookFactory,
            final FeatureModuleContext context) {
        return accountBookFactory.createAccountBook(context).orElseThrow(AssertionError::new);
    }

    @Provides
    @Singleton
    AccountFilter provideAccountFilter(final IAccountBook accountBook) {
        return new AccountFilter(accountBook);
    }
}
