/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.util;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * This configuration enum accesses all values that are read
 * from a settings file in plain text. The settings file cannot be
 * modified at runtime, so the <code>Settings</code> enum is immutable.
 *
 * @author Florian J. Breunig
 */
public enum Settings {

    INSTANCE;

    /**
     * Static initialization of class-dependent logger.
     */
    private static final Logger LOG =
            Logger.getLogger(Settings.class.getName());

    private static final Configuration CONFIG;

    /**
     * The resource in the JAR file to read the settings from.
     */
    private static final String PROPERTIES_RESOURCE
    = "com/moneydance/modules/features/paypalimporter/resources/"
            + "settings.properties";

    static {
        final AbstractFileConfiguration abstractFileConfiguration =
                new PropertiesConfiguration();

        try {
            InputStream inputStream =
                    Helper.INSTANCE.getInputStreamFromResource(
                            PROPERTIES_RESOURCE);
            abstractFileConfiguration.load(inputStream);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        } catch (ConfigurationException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        CONFIG = abstractFileConfiguration;
    }

    /**
     * @return The descriptive name of this extension.
     */
    public String getExtensionName() {
        return CONFIG.getString("extension_name");
    }

    /**
     * @return The icon image that represents this extension.
     */
    public String getIconResource() {
        return CONFIG.getString("icon_resource");
    }

    /**
     * @return The help image.
     */
    public String getHelpResource() {
        return CONFIG.getString("help_resource");
    }

    /**
     * @return The resource that contains the configuration of the logger.
     */
    public String getLoggingPropertiesResource() {
        return CONFIG.getString("logging_properties_resource");
    }

    /**
     * @return The resource in the JAR file to read the language strings from.
     */
    public String getLocalizableResource() {
        return CONFIG.getString("localizable_resource");
    }

    /**
     * @return The suffix of the application event that lets the user start the
     *  import process.
     */
    public String getStartWizardSuffix() {
        return CONFIG.getString("start_wizard_suffix");
    }

    /**
     * @return Tracking code for Google Analytics (aka "utmac").
     */
    public String getTrackingCode() {
        return CONFIG.getString("tracking_code");
    }

    /**
     * @return Event action for installation
     */
    public String getEventActionInstall() {
        return CONFIG.getString("event_action_install");
    }

    /**
     * @return Event action for display
     */
    public String getEventActionDisplay() {
        return CONFIG.getString("event_action_display");
    }

    /**
     * @return Event action for removal
     */
    public String getEventActionUninstall() {
        return CONFIG.getString("event_action_uninstall");
    }

    /**
     * @return Date format specific to PayPal
     */
    public String getDatePattern() {
        return CONFIG.getString("date_pattern");
    }

    /**
     * @return OFX service type specific to PayPal
     */
    public String getServiceType() {
        return CONFIG.getString("service_type");
    }

    /**
     * @return OFX FI Id specific to PayPal
     */
    public String getFIId() {
        return CONFIG.getString("fi_id");
    }

    /**
     * @return OFX FI Org specific to PayPal
     */
    public String getFIOrg() {
        return CONFIG.getString("fi_org");
    }

    /**
     * @return OFX FI Name specific to PayPal
     */
    public String getFIName() {
        return CONFIG.getString("fi_name");
    }

    /**
     * @return OFX FI address specific to PayPal
     */
    public String getFIAddress() {
        return CONFIG.getString("fi_address");
    }

    /**
     * @return OFX FI city specific to PayPal
     */
    public String getFICity() {
        return CONFIG.getString("fi_city");
    }

    /**
     * @return OFX FI URL specific to PayPal
     */
    public String getFIUrl() {
        return CONFIG.getString("fi_url");
    }

    /**
     * @return OFX FI state specific to PayPal
     */
    public String getFIState() {
        return CONFIG.getString("fi_state");
    }

    /**
     * @return OFX FI zip specific to PayPal
     */
    public String getFIZip() {
        return CONFIG.getString("fi_zip");
    }

    /**
     * @return OFX FI country specific to PayPal
     */
    public String getFICountry() {
        return CONFIG.getString("fi_country");
    }

    /**
     * @return Column specification for JGoodies FormLayout
     */
    public String getColumnSpecs() {
        return CONFIG.getString("column_specs");
    }
}
