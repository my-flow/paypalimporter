package com.moneydance.modules.features.paypalimporter.util;

import com.moneydance.modules.features.paypalimporter.bootstrap.Helper;

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
 */
public final class Settings {

    private final Configuration config;
    private final Image iconImage;
    private final DateFormat dateFormat;
    private final Date minDate;
    private final Image helpImage;

    @SuppressWarnings("nullness")
    Settings(final String resource)
            throws IOException, ConfigurationException, ParseException {
            final AbstractFileConfiguration abstractFileConfiguration =
                    new PropertiesConfiguration();
            try (InputStream inputStream =
                         Helper.getInputStreamFromResource(resource)) {
                abstractFileConfiguration.load(inputStream);
            }
        this.config = abstractFileConfiguration;
        this.iconImage = getImage(this.config.getString("icon_resource"));
        this.helpImage = getImage(this.config.getString("help_resource"));
        this.dateFormat = new SimpleDateFormat(
                this.config.getString("date_pattern"),
                Locale.US);
        this.minDate = this.dateFormat.parse(
                this.config.getString("min_date"));
    }

    /**
     * @return The descriptive name of this extension.
     */
    public String getExtensionName() {
        return this.config.getString("extension_name");
    }

    /**
     * @return The icon image that represents this extension.
     */
    public Image getIconImage() {
        return this.iconImage;
    }

    /**
     * @return The help image.
     */
    public Image getHelpImage() {
        return this.helpImage;
    }

    /**
     * @return The resource that contains the configuration of the logger.
     */
    public String getLoggingPropertiesResource() {
        return this.config.getString(
                "logging_properties_resource");
    }

    /**
     * @return The resource in the JAR file to read the language strings from.
     */
    public String getLocalizableResource() {
        return this.config.getString("localizable_resource");
    }

    /**
     * @return The suffix of the application event that lets the user start the
     *  import process.
     */
    public String getStartWizardSuffix() {
        return this.config.getString("start_wizard_suffix");
    }

    /**
     * @return Date format specific to PayPal
     */
    public DateFormat getDateFormat() {
        return (DateFormat) this.dateFormat.clone();
    }

    /**
     * @return Earliest transaction date of the TransactionSearch API
     */
    public Date getMinDate() {
        return new Date(this.minDate.getTime());
    }

    /**
     * @return Error code for seach warning
     */
    public String getErrorCodeSearchWarning() {
        return this.config.getString("error_code_search_warning");
    }

    /**
     * @return OFX service type specific to PayPal
     */
    public String getServiceType() {
        return this.config.getString("service_type");
    }

    /**
     * @return OFX FI TIK ID specific to PayPal
     */
    public String getFITIKId() {
        return this.config.getString("fi_tik_id");
    }

    /**
     * @return OFX FI ID specific to PayPal
     */
    public String getFIId() {
        return this.config.getString("fi_id");
    }

    /**
     * @return OFX FI Org specific to PayPal
     */
    public String getFIOrg() {
        return this.config.getString("fi_org");
    }

    /**
     * @return OFX FI Name specific to PayPal
     */
    public String getFIName() {
        return this.config.getString("fi_name");
    }

    /**
     * @return OFX FI address specific to PayPal
     */
    public String getFIAddress() {
        return this.config.getString("fi_address");
    }

    /**
     * @return OFX FI city specific to PayPal
     */
    public String getFICity() {
        return this.config.getString("fi_city");
    }

    /**
     * @return OFX FI URL specific to PayPal
     */
    public String getFIUrl() {
        return this.config.getString("fi_url");
    }

    /**
     * @return OFX FI state specific to PayPal
     */
    public String getFIState() {
        return this.config.getString("fi_state");
    }

    /**
     * @return OFX FI zip specific to PayPal
     */
    public String getFIZip() {
        return this.config.getString("fi_zip");
    }

    /**
     * @return OFX FI country specific to PayPal
     */
    public String getFICountry() {
        return this.config.getString("fi_country");
    }

    /**
     * @return Column specification for JGoodies Forms
     */
    public String getColumnSpecs() {
        return this.config.getString("column_specs");
    }

    /**
     * @return Rows specification for JGoodies Forms
     */
    public String getRowsSpecs() {
        return this.config.getString("rows_specs");
    }

    private static Image getImage(final String resource) throws IOException {
        try (InputStream inputStream = Helper.getInputStreamFromResource(
                resource)) {
            return ImageIO.read(inputStream);
        }
    }
}
