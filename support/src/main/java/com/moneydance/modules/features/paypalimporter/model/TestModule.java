package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import javax.inject.Singleton;

import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubAccountBookFactory;
import com.moneydance.apps.md.controller.StubContext;

import dagger.Module;
import dagger.Provides;

@Module
public final class TestModule {

    @Provides
    @Singleton
    IAccountBookFactory provideAccountBookFactory(final IAccountBook accountBook) {
        return new StubAccountBookFactory((StubAccountBook) accountBook);
    }

    @Provides
    @Singleton
    IAccountBook provideAccountBook(final FeatureModuleContext context) {
        return ((StubContext) context).getAccountBook();
    }

    @Provides
    @Singleton
    AccountFilter provideAccountFilter(final IAccountBook accountBook) {
        return new AccountFilter(accountBook);
    }
}
