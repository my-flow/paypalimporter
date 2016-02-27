// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.controller;

import com.moneydance.modules.features.paypalimporter.service.RequestHandler;
import com.moneydance.modules.features.paypalimporter.service.ServiceResult;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import javax.annotation.Nullable;

/**
 * Default implementation of the <code>RequestHandler</code> interface.
 *
 * @author Florian J. Breunig
 * @param <V> The type of result objects which the service returns.
 */
abstract class AbstractRequestHandler<V> implements RequestHandler<V> {

    private final ViewController viewController;
    private final Localizable localizable;

    protected AbstractRequestHandler(final ViewController argViewController) {
        this.viewController = argViewController;
        this.localizable = Helper.INSTANCE.getLocalizable();
    }

    @Override
    public final void serviceCallFinished(
            final ServiceResult<V> serviceResult) {

        if (serviceResult.getResults() != null) {
            this.serviceCallSucceeded(serviceResult);
        }
        // no "else" because the case "success + error message" might occur
        final String errorMessage = serviceResult.getErrorMessage();
        if (errorMessage != null) {
            this.serviceCallFailed(
                    serviceResult.getErrorCode(),
                    errorMessage);
        }
    }

    /**
     * @param serviceResult result must not be null
     */
    protected abstract void serviceCallSucceeded(
            final ServiceResult<V> serviceResult);

    private void serviceCallFailed(
            @Nullable final String errorCode,
            final String originalMessage) {

        final String translatedMessage =
                this.localizable.getTranslatedErrorMessage(errorCode);
        final String displayMessage;
        if (translatedMessage == null) {
            displayMessage = originalMessage;
        } else {
            displayMessage = translatedMessage;
        }

        final String labelMessage =
                this.localizable.getErrorMessageServiceCallFailed(
                        displayMessage);
        this.viewController.unlock(labelMessage, errorCode);
    }

    protected final ViewController getViewController() {
        return this.viewController;
    }
}
