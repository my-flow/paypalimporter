package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.util.Optional;

public interface IAccountBookFactory {

    Optional<IAccountBook> createAccountBook(FeatureModuleContext context);
}
