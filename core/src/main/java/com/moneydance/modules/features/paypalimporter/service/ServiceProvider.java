// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.service;

import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.paypal.core.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

/**
 * Facade for initiating service calls. Also supports shutdown.
 *
 * @author Florian J. Breunig
 */
public final class ServiceProvider {

    /**
     * Static initialization of class-dependent logger.
     */
    static final Logger LOG = Logger.getLogger(
            ServiceProvider.class.getName());

    /**
     * The resource in the JAR file or file system to read the properties from.
     */
    private static final String PROPERTIES_RESOURCE = "sdk_config.properties";

    private final Preferences prefs;
    private ExecutorService executorService;

    public ServiceProvider() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.prefs = Helper.INSTANCE.getPreferences();
    }

    public void callCheckCurrencyService(
            final String username,
            final char[] password,
            final String signature,
            final RequestHandler<CurrencyCodeType> requestHandler) {

        Callable<ServiceResult<CurrencyCodeType>> callable =
                new CheckCurrencyService(
                        this.createService(username, password, signature),
                        this.prefs.getLocale());
        this.createAndExecuteFutureTask(callable, requestHandler);
    }

    public void callTransactionSearchService(
            final String username,
            final char[] password,
            final String signature,
            final Date startDate,
            final Date endDate,
            final CurrencyCodeType currencyCode,
            final RequestHandler<PaymentTransactionSearchResultType>
            requestHandler) {

        Callable<ServiceResult<PaymentTransactionSearchResultType>>
        callable = new TransactionSearchService(
                this.createService(username, password, signature),
                currencyCode,
                startDate,
                endDate,
                this.prefs.getLocale());

        this.createAndExecuteFutureTask(callable, requestHandler);
    }

    /**
     * Shuts down all running requests. Can be called anytime.
     */
    public void shutdownNow() {
        synchronized (this.prefs) {
            this.executorService.shutdownNow();
            this.executorService = Executors.newSingleThreadExecutor();
        }
    }

    private PayPalAPIInterfaceServiceService createService(
            final String username,
            final char[] password,
            final String signature) {
        final Properties config = new Properties();
        try {
            InputStream inputStream =
                    Helper.getInputStreamFromResource(PROPERTIES_RESOURCE);
            config.load(inputStream);
        } catch (IllegalArgumentException e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.WARNING, message, e);
            }
        } catch (IOException e) {
            final String message = e.getMessage();
            if (message != null) {
                LOG.log(Level.WARNING, message, e);
            }
        }

        final String prefix = Constants.ACCOUNT_PREFIX + 1;

        config.setProperty(
                prefix + Constants.CREDENTIAL_USERNAME_SUFFIX,
                username);
        config.setProperty(
                prefix + Constants.CREDENTIAL_PASSWORD_SUFFIX,
                String.valueOf(password));
        config.setProperty(
                prefix + Constants.CREDENTIAL_SIGNATURE_SUFFIX,
                signature);

        if (this.prefs.hasProxy()) {
            config.setProperty(
                    Constants.USE_HTTP_PROXY,
                    String.valueOf(this.prefs.hasProxyAuthentication()));
            config.setProperty(
                    Constants.HTTP_PROXY_HOST,
                    String.valueOf(this.prefs.getProxyHost()));
            config.setProperty(
                    Constants.HTTP_PROXY_PORT,
                    String.valueOf(this.prefs.getProxyPort()));
            if (this.prefs.hasProxyAuthentication()) {
                config.setProperty(
                        Constants.HTTP_PROXY_USERNAME,
                        String.valueOf(this.prefs.getProxyUsername()));
                config.setProperty(
                        Constants.HTTP_PROXY_PASSWORD,
                        String.valueOf(this.prefs.getProxyPassword()));
            }
        }
        return new PayPalAPIInterfaceServiceService(config);
    }

    private <V> void createAndExecuteFutureTask(
            final Callable<ServiceResult<V>> callable,
            final RequestHandler<V> requestHandler) {

        final FutureTask<ServiceResult<V>> task =
                new FutureTask<ServiceResult<V>>(callable) {
            @Override
            protected void done() {
                ServiceResult<V> serviceResult = null;
                try {
                    serviceResult = this.get();
                } catch (InterruptedException e) {
                    LOG.log(Level.WARNING, "Thread interrupted", e);
                    serviceResult = new ServiceResult<V>(
                            e.getLocalizedMessage());
                } catch (ExecutionException e) {
                    final Throwable cause = e.getCause();
                    if (cause != null) {
                        LOG.log(Level.WARNING, "Task aborted", cause);
                    }
                    serviceResult = new ServiceResult<V>(
                            e.getLocalizedMessage());
                } finally {
                    final ServiceResult<V> finalResult = serviceResult;
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                requestHandler.serviceCallFinished(finalResult);
                            }
                        });
                    } catch (InterruptedException e) {
                        LOG.log(Level.WARNING, "Thread interrupted", e);
                    } catch (InvocationTargetException e) {
                        LOG.log(Level.WARNING, "Exception thrown", e);
                    }
                }
            }
        };
        synchronized (this.prefs) {
            this.executorService.submit(task);
        }
    }
}
