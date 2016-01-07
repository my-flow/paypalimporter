// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.moneydance.apps.md.controller.StubAccountBook;
import com.moneydance.apps.md.controller.StubContext;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class AccountBookFactoryImplTest extends TestCase {

    @Test
    public void testCreateAccountBook() {
        IAccountBookFactory factory = AccountBookFactoryImpl.INSTANCE;
        StubContext context = new StubContext(null, new StubAccountBook(null));
        factory.createAccountBook(context);
    }
}
