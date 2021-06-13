// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import java.util.Optional;

/**
 * @author Florian J. Breunig
 */
public interface IAccountBookFactory {

    Optional<IAccountBook> createAccountBook(FeatureModuleContext context);
}
