// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;

/**
 * @author Florian J. Breunig
 */
public final class StubAccountBookFactory implements IAccountBookFactory {

    private final StubAccountBook accountBook;

    public StubAccountBookFactory(final StubAccountBook argAccountBook) {
        this.accountBook = argAccountBook;
    }

    @Override
    public StubAccountBook createAccountBook(
            final FeatureModuleContext context) {
        return this.accountBook;
    }

}
