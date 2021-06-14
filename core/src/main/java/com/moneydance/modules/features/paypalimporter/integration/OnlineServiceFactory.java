// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.moneydance.model.OnlineService;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
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

    private final Settings settings;
    private final OnlineService initializedOnlineService;

    public OnlineServiceFactory(final Settings argSettings) {
        this.settings = argSettings;
        this.initializedOnlineService = createService();
    }

    public PayPalOnlineService createService(
            final IAccountBook accountBook) {
        final OnlineInfo onlineInfo = accountBook.getOnlineInfo();

        OnlineService onlineService = getServiceById(onlineInfo, initializedOnlineService);

        if (onlineService == null) {
            onlineService = new InitializedOnlineService(
                    accountBook.getWrappedOriginal(),
                    this.settings,
                    new Date());
        }
        return new PayPalOnlineService(
                onlineService,
                this.settings.getFIId());
    }

    public void removeService(final IAccountBook accountBook) {
        final OnlineInfo onlineInfo = accountBook.getOnlineInfo();

        for (OnlineService service : onlineInfo.getAllServices()) {
            if (this.settings.getServiceType().equals(service.getServiceType())) {
                service.clearAuthenticationCache();
                if (!accountBook.logRemovedItem(service)) {
                    LOG.warning(String.format(
                        "Could not remove %s online initializedOnlineService %s",
                        service.getServiceType(),
                        service.getServiceId()));
                }
            }
        }
    }

    private OnlineService createService() {
        return new InitializedOnlineService(
                null, // temporary account
                new StreamTable(),
                this.settings,
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
