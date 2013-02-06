/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.DateRange;

/**
 * @author Florian J. Breunig
 */
public interface InputData {

    String getUsername();

    char[] getPassword(final boolean clear);

    String getSignature();

    int getAccountId();

    DateRange getDateRange();
}
