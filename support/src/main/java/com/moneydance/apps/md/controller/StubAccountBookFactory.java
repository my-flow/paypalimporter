package com.moneydance.apps.md.controller;

import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.model.IAccountBookFactory;

import java.util.Optional;

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
