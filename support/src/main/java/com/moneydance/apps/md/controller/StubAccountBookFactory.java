// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.apps.md.controller;

import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;

import java.util.Optional;

/**
 * @author Florian J. Breunig
 */
public final class StubAccountBookFactory implements IAccountBookFactory {

    private final StubAccountBook accountBook;

    public StubAccountBookFactory(final StubAccountBook argAccountBook) {
        this.accountBook = argAccountBook;
    }

    @Override
    public Optional<IAccountBook> createAccountBook(
            final FeatureModuleContext context) {
        return Optional.ofNullable(this.accountBook);
    }

}
