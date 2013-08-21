// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * This i18n class provides language-dependent strings such as labels and
 * messages.
 *
 * @author Florian J. Breunig
 */
public final class Localizable {

    private final ResourceBundle resourceBundle;

    Localizable(final String localizableResource, final Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle(
                localizableResource,
                locale);
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    /**
     * @return the text of the main toolbar's button used to invoke the wizard.
     */
    public String getLabelButtonText() {
        return this.resourceBundle.getString("label_button_text");
    }

    /**
     * @return the name of the "Continue" button.
     */
    public String getLabelContinueButton() {
        return this.resourceBundle.getString("label_continue_button");
    }

    /**
     * @return the name of the created PayPal account.
     */
    public String getNameNewAccount() {
        return this.resourceBundle.getString("name_new_account");
    }

    /**
     * @return the URL of the created PayPal account.
     */
    public String getUrlNewAccount() {
        return this.resourceBundle.getString("url_new_account");
    }

    /**
     * @return the URL that is displayed if the user needs help.
     */
    public URL getUrlHelp() {
        try {
            return new URL(this.resourceBundle.getString("url_help"));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Could not parse help URL", e);
        }
    }

    /**
     * @return the question when multiple currencies are available
     */
    public String getQuestionMessageMultipleCurrencies(
            final String currency,
            final Object[] currencies) {
        final String templateString = this.resourceBundle.getString(
                "question_message_multiple_currencies");

        Map<String, String> valuesMap =
                new ConcurrentHashMap<String, String>(2);
        valuesMap.put("currency", currency);
        valuesMap.put("currencies", StringUtils.join(currencies, ", "));
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        return sub.replace(templateString);
    }

    /**
     * @return the error message when no root account is not available
     */
    public String getErrorMessageRootAccountNull() {
        return this.resourceBundle.getString("error_message_root_account_null");
    }

    /**
     * @return the error message when no username has been entered
     */
    public String getErrorMessageUsernameBlank() {
        return this.resourceBundle.getString("error_message_username_blank");
    }

    /**
     * @return the error message when no password has been entered
     */
    public String getErrorMessagePasswordBlank() {
        return this.resourceBundle.getString("error_message_password_blank");
    }

    /**
     * @return Error message when no signature has been entered
     */
    public String getErrorMessageSignatureBlank() {
        return this.resourceBundle.getString("error_message_signature_blank");
    }

    /**
     * @return the error message when start date is not before end date
     */
    public String getErrorMessageStartDateNotBeforeEndDate() {
        return this.resourceBundle.getString(
                "error_message_start_date_not_before_end_date");
    }

    /**
     * @return the error message when service call failed
     */
    public String getErrorMessageServiceCallFailed(final String errorMessage) {
        final String templateString = this.resourceBundle.getString(
                "error_message_service_call_failed");

        Map<String, String> valuesMap =
                new ConcurrentHashMap<String, String>(1);
        valuesMap.put("error.message", errorMessage);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        return sub.replace(templateString);
    }

    /**
     * @return Error message when no connection could be established.
     */
    public String getErrorMessageConnectionFailed() {
        return this.resourceBundle.getString("error_message_connection_failed");
    }
}
