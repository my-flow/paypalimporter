// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Florian J. Breunig
 */
@RunWith(Suite.class)
@SuiteClasses({
    AccountDelegateListenerTest.class,
    CheckCurrencyRequestHandlerTest.class,
    ComponentDelegateListenerTest.class,
    TransactionSearchIteratorTest.class,
    TransactionSearchRequestHandlerTest.class,
})
public final class AllUnitTests {
    // no test cases
}
