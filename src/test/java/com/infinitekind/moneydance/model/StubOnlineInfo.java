// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2015 Florian J. Breunig. All rights reserved.

package com.infinitekind.moneydance.model;

import com.moneydance.modules.features.paypalimporter.model.IAccountBook;

import java.util.Collections;
import java.util.List;

/**
 * @author Florian J. Breunig
 */
public final class StubOnlineInfo extends OnlineInfo {

    private final List<OnlineService> serviceList;

    public StubOnlineInfo(final IAccountBook accountBook,
            final List<OnlineService> argServiceList) {
        super(accountBook.getWrappedOriginal());
        this.serviceList = Collections.unmodifiableList(argServiceList);
    }

    @Override
    public List<OnlineService> getAllServices() {
        return this.serviceList;
    }
}
