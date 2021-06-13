// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

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
