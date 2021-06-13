// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

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
