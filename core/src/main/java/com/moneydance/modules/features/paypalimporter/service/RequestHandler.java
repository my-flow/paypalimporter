package com.moneydance.modules.features.paypalimporter.service;

/**
 * Public interface for all requests handlers in this project.
 *
 * @param <V> The type of result objects which the service returns.
 */
public interface RequestHandler<V> {
    void serviceCallFinished(ServiceResult<V> serviceResult);
}
