// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import org.apache.commons.lang3.Validate;

import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

/**
 * @author Florian J. Breunig
 * @param <V>
 */
abstract class AbstractRequestHandler<V> implements RequestHandler<V> {

    private final ViewController viewController;
    private final Localizable localizable;

    AbstractRequestHandler(final ViewController argViewController) {
        Validate.notNull(argViewController, "view controller must not be null");
        this.viewController = argViewController;
        this.localizable = Helper.INSTANCE.getLocalizable();
    }

    @Override
    public void serviceCallFinished(final ServiceResult<V> serviceResult) {
        if (serviceResult.getResults() != null) {
            this.serviceCallSucceeded(serviceResult);
        }
        if (serviceResult.getErrorMessage() != null) {
            this.serviceCallFailed(serviceResult.getErrorMessage());
        }
    }

    /**
     * @param serviceResult result must not be null
     */
    protected abstract void serviceCallSucceeded(
            final ServiceResult<V> serviceResult);

    private void serviceCallFailed(final String message) {
        final String labelMessage =
                this.localizable.getErrorMessageServiceCallFailed(message);
        this.viewController.unlock(labelMessage, null);
    }

    protected final ViewController getViewController() {
        return this.viewController;
    }
}
