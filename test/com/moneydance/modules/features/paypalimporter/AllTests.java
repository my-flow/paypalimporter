/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Florian J. Breunig
 */
@RunWith(Suite.class)
@SuiteClasses({
    MainTest.class,
    com.moneydance.modules.features.paypalimporter.controller.AllTests.class,
    com.moneydance.modules.features.paypalimporter.domain.AllTests.class,
    com.moneydance.modules.features.paypalimporter.integration.AllTests.class,
    com.moneydance.modules.features.paypalimporter.model.AllTests.class,
    com.moneydance.modules.features.paypalimporter.service.AllTests.class,
    com.moneydance.modules.features.paypalimporter.util.AllTests.class,
    com.moneydance.modules.features.paypalimporter.presentation.AllTests.class})
public final class AllTests {
    // no test cases
}
