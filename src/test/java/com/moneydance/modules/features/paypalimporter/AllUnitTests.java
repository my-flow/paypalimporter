// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Florian J. Breunig
 */
@RunWith(Suite.class)
@SuiteClasses({
    com.moneydance.modules.features.paypalimporter.controller.AllUnitTests.class,
    com.moneydance.modules.features.paypalimporter.domain.AllUnitTests.class,
    com.moneydance.modules.features.paypalimporter.integration.AllUnitTests.class,
    com.moneydance.modules.features.paypalimporter.model.AllUnitTests.class,
    com.moneydance.modules.features.paypalimporter.service.AllUnitTests.class,
    com.moneydance.modules.features.paypalimporter.util.AllUnitTests.class,
})
public final class AllUnitTests {
    // no test cases
}
