// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.util.StreamTable;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashMap;
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
    private static final String KEY_SERVICE_TYPE = "type";

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
            onlineService = new OnlineService(accountBook.getWrappedOriginal());
            setUpOnlineService(onlineService);
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
        @SuppressWarnings("nullness")
        final OnlineService onlineService = new OnlineService(
                null, // temporary account
                new StreamTable());
        setUpOnlineService(onlineService);
        return onlineService;
    }

    @SuppressWarnings("initialization")
    private static void setUpOnlineService(final OnlineService onlineService) {
        final Settings settings = Helper.INSTANCE.getSettings();
        onlineService.addParameters(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                this.put(KEY_SERVICE_TYPE, settings.getServiceType());
            }
        });
        onlineService.setFIId(settings.getFIId());
        onlineService.setFIOrg(settings.getFIOrg());
        onlineService.setFIName(settings.getFIName());
        onlineService.setFIAddress1(settings.getFIAddress());
        onlineService.setFICity(settings.getFICity());
        onlineService.setFIState(settings.getFIState());
        onlineService.setFIZip(settings.getFIZip());
        onlineService.setFICountry(settings.getFICountry());
        onlineService.setFIUrl(settings.getFIUrl());
        onlineService.setDateUpdated(new Date().getTime());
    }

    @Nullable private static OnlineService getServiceById(
            final OnlineInfo onlineInfo, final OnlineService service) {

        for (OnlineService onlineService : onlineInfo.getAllServices()) {
            if (onlineService.isSameAs(service)) {
                return onlineService;
            }
        }
        return null;
    }
}
