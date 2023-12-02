package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.OnlineInfo;
import com.infinitekind.moneydance.model.OnlineService;
import com.infinitekind.util.StreamTable;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Use this factory class in order to access and remove the
 * <code>PayPalOnlineService</code> for a given <code>IAccountBook</code>.
 */
public final class OnlineServiceFactory {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            OnlineServiceFactory.class.getName());

    private final Settings settings;
    private OnlineService initializedOnlineService;

    public OnlineServiceFactory(final Settings argSettings) {
        this.settings = argSettings;
    }

    public PayPalOnlineService createService(
            final IAccountBook accountBook) {
        final OnlineInfo onlineInfo = accountBook.getOnlineInfo();

        if (this.initializedOnlineService == null) {
            this.initializedOnlineService = new InitializedOnlineService(
                    accountBook.getWrappedOriginal(),
                    new StreamTable(),
                    this.settings,
                    new Date());
        }

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
