// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.util.Optional;

/**
 * @author Florian J. Breunig
 */
public interface IAccountBookFactory {

    Optional<IAccountBook> createAccountBook(FeatureModuleContext context);
}
