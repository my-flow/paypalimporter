// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * A facade for dispatching Google Analytics tracking information.
 *
 * @author Florian J. Breunig
 */
public final class Tracker {

    private final String fullVersion;
    private final String build;

    private final JGoogleAnalyticsTracker analyticsTracker;

    Tracker(final int argBuild,
            final String argExtensionName,
            final String argFullVersion,
            final String argTrackingCode) {
        this.fullVersion = String.format("Moneydance %s", argFullVersion);
        this.build       = String.format("%s v%d", argExtensionName, argBuild);

        if (!Proxy.NO_PROXY.equals(Tracker.getProxy())) {
            JGoogleAnalyticsTracker.setProxy(Tracker.getProxy());
        }
        AnalyticsConfigData config = new AnalyticsConfigData(argTrackingCode);
        this.analyticsTracker = new JGoogleAnalyticsTracker(
                config,
                GoogleAnalyticsVersion.V_4_7_2);
    }

    public void track(final EventName eventName) {
        this.analyticsTracker.trackEvent(
                this.fullVersion,
                eventName.toString(),
                this.build);
    }

    public void track(final String eventName) {
        this.analyticsTracker.trackEvent(
                this.fullVersion,
                eventName,
                this.build);
    }

    private static Proxy getProxy() {
        final Preferences prefs = Helper.INSTANCE.getPreferences();
        if (!prefs.hasProxy()) {
            return Proxy.NO_PROXY;
        }

        final SocketAddress socketAddress = new InetSocketAddress(
                prefs.getProxyHost(),
                prefs.getProxyPort());

        boolean authProxy = prefs.hasProxyAuthentication();

        Proxy.Type proxyType = Proxy.Type.HTTP;

        if (authProxy) {
            proxyType = Proxy.Type.SOCKS;
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            prefs.getProxyUsername(),
                            prefs.getProxyPassword().toCharArray());
                }
            });
        }
        return new Proxy(proxyType, socketAddress);
    }

    /**
     * @author Florian J. Breunig
     */
    public enum EventName {

        INSTALL(Helper.INSTANCE.getSettings().getEventActionInstall()),

        DISPLAY(Helper.INSTANCE.getSettings().getEventActionDisplay()),

        UNINSTALL(Helper.INSTANCE.getSettings().getEventActionUninstall());

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
