// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import com.brsanthu.googleanalytics.EventHit;
import com.brsanthu.googleanalytics.GoogleAnalytics;
import com.brsanthu.googleanalytics.GoogleAnalyticsConfig;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.lang3.StringUtils;

/**
 * A facade for dispatching Google Analytics tracking information.
 *
 * @author Florian J. Breunig
 */
public final class Tracker {

    private final String fullVersion;
    private final String build;

    private final GoogleAnalytics analyticsTracker;

    Tracker(final int argBuild,
            final String argExtensionName,
            final String argFullVersion,
            final String argTrackingCode) {
        this.fullVersion = String.format("Moneydance %s", argFullVersion);
        this.build       = String.format("%s v%d", argExtensionName, argBuild);

        Settings settings = Helper.INSTANCE.getSettings();
        boolean enabled = StringUtils.isNotEmpty(settings.getTrackingCode());

        GoogleAnalyticsConfig config = new GoogleAnalyticsConfig();
        config.setEnabled(enabled);

        final Preferences prefs = Helper.INSTANCE.getPreferences();
        if (prefs.hasProxy()) {
            config.setProxyHost(prefs.getProxyHost());
            config.setProxyPort(prefs.getProxyPort());

            if (prefs.hasProxyAuthentication()) {
                config.setProxyUserName(prefs.getProxyUsername());
                config.setProxyPassword(prefs.getProxyPassword());
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                prefs.getProxyUsername(),
                                prefs.getProxyPassword().toCharArray());
                    }
                });
            }
        }
        this.analyticsTracker = new GoogleAnalytics(config, argTrackingCode);
    }

    @SuppressWarnings("nullness")
    public void track(final EventName eventName) {
        this.analyticsTracker.postAsync(new EventHit(
                this.fullVersion,
                eventName.toString(),
                this.build,
                null));
    }

    @SuppressWarnings("nullness")
    public void track(final String eventName) {
        this.analyticsTracker.postAsync(new EventHit(
                this.fullVersion,
                eventName,
                this.build,
                null));
    }

    /**
     * @author Florian J. Breunig
     */
    public enum EventName {

        INSTALL(Helper.INSTANCE.getSettings().getEventActionInstall()),

        DISPLAY(Helper.INSTANCE.getSettings().getEventActionDisplay()),

        UNINSTALL(Helper.INSTANCE.getSettings().getEventActionUninstall());

        private final String eventString;

        EventName(final String argEventString) {
            this.eventString = argEventString;
        }

        @Override
        public String toString() {
            return this.eventString;
        }
    }
}
