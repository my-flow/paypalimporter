// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.moneydance.apps.md.model.OnlineInfo;
import com.moneydance.apps.md.model.OnlineService;
import com.moneydance.apps.md.model.RootAccount;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Settings;
import com.moneydance.util.StreamTable;

import java.util.Date;

import org.apache.commons.lang3.Validate;

/**
 * @author Florian J. Breunig
 */
public final class OnlineServiceFactory {

    private static final String SERVICE_ID = createServiceId();
    private static final String KEY_SERVICE_TYPE = "type";

    /**
     * Restrictive constructor.
     */
    private OnlineServiceFactory() {
        // Prevents this class from being instantiated from the outside.
    }

    public static PayPalOnlineService getService(
            final RootAccount rootAccount) {
        Validate.notNull(rootAccount, "root account must not be null");
        final OnlineInfo onlineInfo = rootAccount.getOnlineInfo();

        OnlineService onlineService = onlineInfo.getServiceById(SERVICE_ID);

        if (onlineService == null) {
            onlineService = new OnlineService(
                    onlineInfo,
                    new StreamTable());
            onlineInfo.addService(onlineService);
            setUpOnlineService(onlineService);
        }
        return new PayPalOnlineService(onlineService);
    }

    public static void removeService(final RootAccount rootAccount) {
        Validate.notNull(rootAccount, "root account must not be null");
        final OnlineInfo onlineInfo = rootAccount.getOnlineInfo();

        int serviceCount = onlineInfo.getServiceCount();
        for (int i = 0; i < serviceCount; i++) {
            final OnlineService service = onlineInfo.getService(i);
            if (service != null && service.isSameAs(SERVICE_ID)) {
                service.clearAuthenticationCache();
                onlineInfo.removeService(i);
            }
        }
    }

    private static String createServiceId() {
        final OnlineService onlineService = new OnlineService(
                null, // temporary account
                new StreamTable());
        setUpOnlineService(onlineService);
        return onlineService.getServiceId();
    }

    private static void setUpOnlineService(final OnlineService onlineService) {
        final StreamTable table = new StreamTable(1);
        final Settings settings = Helper.INSTANCE.getSettings();
        table.put(KEY_SERVICE_TYPE, settings.getServiceType());
        onlineService.mergeDataTables(table);
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
}
