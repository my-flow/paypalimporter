// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.logging.Logger;

/**
 * @author Florian J. Breunig
 */
public final class Tracker {

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG = Logger.getLogger(
            Tracker.class.getName());

    private final Preferences prefs;
    private final String fullVersion;
    private final String build;

    private final JGoogleAnalyticsTracker analyticsTracker;

    Tracker(final int argBuild) {
        this.prefs       = Helper.INSTANCE.getPreferences();
        this.fullVersion = String.format("Moneydance %s",
                this.prefs.getFullVersion());
        this.build       = String.format("%s v%d",
                Settings.getExtensionName(),
                argBuild);

        JGoogleAnalyticsTracker.setProxy(this.getProxy());
        AnalyticsConfigData config = new AnalyticsConfigData(
                Settings.getTrackingCode());
        this.analyticsTracker = new JGoogleAnalyticsTracker(
                config,
                GoogleAnalyticsVersion.V_4_7_2);
    }

    public void track(final EventName eventName) {
        LOG.config("trackEvent");
        this.analyticsTracker.trackEvent(
                this.fullVersion,
                eventName.toString(),
                this.build);
    }

    private Proxy getProxy() {
        if (!this.prefs.hasProxy()) {
            return Proxy.NO_PROXY;
        }

        final SocketAddress socketAddress = new InetSocketAddress(
                this.prefs.getProxyHost(),
                this.prefs.getProxyPort());

        boolean authProxy = this.prefs.hasProxyAuthentication();

        Proxy.Type proxyType = Proxy.Type.HTTP;

        if (authProxy) {
            proxyType = Proxy.Type.SOCKS;
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            Tracker.this.prefs.getProxyUsername(),
                            Tracker.this.prefs.getProxyPassword()
                            .toCharArray());
                }
            });
        }
        return new Proxy(proxyType, socketAddress);
    }

    /**
     * @author Florian J. Breunig
     */
    public enum EventName {

        INSTALL(Settings.getEventActionInstall()),

        DISPLAY(Settings.getEventActionDisplay()),

        UNINSTALL(Settings.getEventActionUninstall());

        private final String eventString;

        private EventName(final String argEventString) {
            this.eventString = argEventString;
        }

        @Override
        public String toString() {
            return this.eventString;
        }
    }
}
