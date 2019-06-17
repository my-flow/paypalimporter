// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

/**
 * Default implementation of the <code>RequestHandler</code> interface.
 *
 * @author Florian J. Breunig
 * @param <V> The type of result objects which the service returns.
 */
abstract class AbstractRequestHandler<V> implements RequestHandler<V> {

    private final ViewController viewController;
    private final Localizable localizable;

    AbstractRequestHandler(
            final ViewController argViewController,
            final Localizable argLocalizable) {
        this.viewController = argViewController;
        this.localizable = argLocalizable;
    }

    @Override
    public final void serviceCallFinished(
            final ServiceResult<V> serviceResult) {

        serviceResult
                .getResults()
                .ifPresent(results -> this.serviceCallSucceeded(serviceResult));

        // the case "success + error message" might occur
        serviceResult
                .getErrorMessage()
                .ifPresent(errorMessage -> this.serviceCallFailed(
                    serviceResult
                            .getErrorCode()
                            .orElse(null),
                    errorMessage));
    }

    /**
     * @param serviceResult result must not be null
     */
    protected abstract void serviceCallSucceeded(
            final ServiceResult<V> serviceResult);

    private void serviceCallFailed(
            final String errorCode,
            final String originalMessage) {

        final String displayMessage = this.localizable
                .getTranslatedErrorMessage(errorCode)
                .orElse(originalMessage);

        final String labelMessage =
                this.localizable.getErrorMessageServiceCallFailed(
                        displayMessage);
        this.viewController.unlock(labelMessage, errorCode);
    }

    final ViewController getViewController() {
        return this.viewController;
    }
}
