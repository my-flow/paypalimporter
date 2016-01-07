// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * This configuration class accesses all values that are read
 * from a settings file in plain text. The settings file cannot be
 * modified at runtime, so the <code>Settings</code> class is effectively
 * immutable.
 *
 * @author Florian J. Breunig
 */
public final class Settings {

    /**
     * The resource in the JAR file to read the settings from.
     */
    private static final String PROPERTIES_RESOURCE = "settings.properties";

    private final Configuration config;
    private Image iconImage;
    private DateFormat dateFormat;
    private Date minDate;
    private Image helpImage;

    Settings() {
        final AbstractFileConfiguration abstractFileConfiguration =
                new PropertiesConfiguration();

        try {
            InputStream inputStream = Helper.getInputStreamFromResource(
                    PROPERTIES_RESOURCE);
            abstractFileConfiguration.load(inputStream);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        this.config = abstractFileConfiguration;
    }

    /**
     * @return The descriptive name of this extension.
     */
    public String getExtensionName() {
        return this.config.getString("extension_name"); //$NON-NLS-1$
    }

    /**
     * @return The icon image that represents this extension.
     */
    public Image getIconImage() {
        if (this.iconImage == null) {
            this.iconImage = getImage(
                    this.config.getString("icon_resource")); //$NON-NLS-1$
        }
        return this.iconImage;
    }

    /**
     * @return The help image.
     */
    public Image getHelpImage() {
        if (this.helpImage == null) {
            this.helpImage = getImage(
                    this.config.getString("help_resource")); //$NON-NLS-1$
        }
        return this.helpImage;
    }

    /**
     * @return The resource that contains the configuration of the logger.
     */
    public String getLoggingPropertiesResource() {
        return this.config.getString(
                "logging_properties_resource"); //$NON-NLS-1$
    }

    /**
     * @return The resource in the JAR file to read the language strings from.
     */
    public String getLocalizableResource() {
        return this.config.getString("localizable_resource"); //$NON-NLS-1$
    }

    /**
     * @return The suffix of the application event that lets the user start the
     *  import process.
     */
    public String getStartWizardSuffix() {
        return this.config.getString("start_wizard_suffix"); //$NON-NLS-1$
    }

    /**
     * @return Tracking code for Google Analytics (aka "utmac").
     */
    public String getTrackingCode() {
        return this.config.getString("tracking_code"); //$NON-NLS-1$
    }

    /**
     * @return Event action for installation
     */
    public String getEventActionInstall() {
        return this.config.getString("event_action_install"); //$NON-NLS-1$
    }

    /**
     * @return Event action for display
     */
    public String getEventActionDisplay() {
        return this.config.getString("event_action_display"); //$NON-NLS-1$
    }

    /**
     * @return Event action for removal
     */
    public String getEventActionUninstall() {
        return this.config.getString("event_action_uninstall"); //$NON-NLS-1$
    }

    /**
     * @return Date format specific to PayPal
     */
    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(
                    this.config.getString("date_pattern"), //$NON-NLS-1$
                    Locale.US);
        }
        return this.dateFormat;
    }

    /**
     * @return Earliest transaction date of the TransactionSearch API
     */
    public Date getMinDate() {
        if (this.minDate == null) {
            try {
                this.minDate = this.getDateFormat().parse(
                        this.config.getString("min_date")); //$NON-NLS-1$
            } catch (ParseException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return this.minDate;
    }

    /**
     * @return Error code for seach warning
     */
    public String getErrorCodeSearchWarning() {
        return this.config.getString("error_code_search_warning"); //$NON-NLS-1$
    }

    /**
     * @return OFX service type specific to PayPal
     */
    public String getServiceType() {
        return this.config.getString("service_type"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI Id specific to PayPal
     */
    public String getFIId() {
        return this.config.getString("fi_id"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI Org specific to PayPal
     */
    public String getFIOrg() {
        return this.config.getString("fi_org"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI Name specific to PayPal
     */
    public String getFIName() {
        return this.config.getString("fi_name"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI address specific to PayPal
     */
    public String getFIAddress() {
        return this.config.getString("fi_address"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI city specific to PayPal
     */
    public String getFICity() {
        return this.config.getString("fi_city"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI URL specific to PayPal
     */
    public String getFIUrl() {
        return this.config.getString("fi_url"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI state specific to PayPal
     */
    public String getFIState() {
        return this.config.getString("fi_state"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI zip specific to PayPal
     */
    public String getFIZip() {
        return this.config.getString("fi_zip"); //$NON-NLS-1$
    }

    /**
     * @return OFX FI country specific to PayPal
     */
    public String getFICountry() {
        return this.config.getString("fi_country"); //$NON-NLS-1$
    }

    /**
     * @return Column specification for JGoodies FormLayout
     */
    public String getColumnSpecs() {
        return this.config.getString("column_specs"); //$NON-NLS-1$
    }

    private static Image getImage(final String resource) {
        try {
            InputStream inputStream = Helper.getInputStreamFromResource(
                    resource);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
