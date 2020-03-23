// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2020 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

/**
 * Public interface for all requests handlers in this project.
 *
 * @author Florian J. Breunig
 * @param <V> The type of result objects which the service returns.
 */
public interface RequestHandler<V> {
    void serviceCallFinished(ServiceResult<V> serviceResult);
}
