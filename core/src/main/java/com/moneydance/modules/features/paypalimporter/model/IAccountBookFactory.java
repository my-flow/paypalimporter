// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.FeatureModuleContext;

import javax.annotation.Nullable;

/**
 * @author Florian J. Breunig
 */
public interface IAccountBookFactory {

    @Nullable IAccountBook createAccountBook(final FeatureModuleContext context);

}
