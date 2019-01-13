// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.util.StreamTable;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Use this factory class in order to access and remove the
 * <code>PayPalOnlineService</code> for a given <code>IAccountBook</code>.
 *
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactory {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            OnlineServiceFactory.class.getName());

    private static final OnlineService SERVICE = createService();

    /**
     * Restrictive constructor.
     */
    private OnlineServiceFactory() {
        // Prevents this class from being instantiated from the outside.
    }

    public static PayPalOnlineService createService(
            final IAccountBook accountBook) {
        final OnlineInfo onlineInfo = accountBook.getOnlineInfo();

        OnlineService onlineService = getServiceById(onlineInfo, SERVICE);

        if (onlineService == null) {
            onlineService = new InitializedOnlineService(
                    accountBook.getWrappedOriginal(),
                    Helper.INSTANCE.getSettings(),
                    new Date());
        }
        return new PayPalOnlineService(onlineService);
    }

    public static void removeService(final IAccountBook accountBook) {
        final OnlineInfo onlineInfo = accountBook.getOnlineInfo();

        final Settings settings = Helper.INSTANCE.getSettings();

        for (OnlineService service : onlineInfo.getAllServices()) {
            if (settings.getServiceType().equals(service.getServiceType())) {
                service.clearAuthenticationCache();
                if (!accountBook.logRemovedItem(service)) {
                    LOG.warning(String.format(
                        "Could not remove %s online service %s",
                        service.getServiceType(),
                        service.getServiceId()));
                }
            }
        }
    }

    private static OnlineService createService() {
        return new InitializedOnlineService(
                null, // temporary account
                new StreamTable(),
                Helper.INSTANCE.getSettings(),
                new Date());
    }

    @Nullable private static OnlineService getServiceById(
            final OnlineInfo onlineInfo, final OnlineService service) {

        return onlineInfo
                .getAllServices()
                .stream()
                .filter(onlineService -> onlineService.isSameAs(service))
                .findFirst()
                .orElse(null);
    }
}
