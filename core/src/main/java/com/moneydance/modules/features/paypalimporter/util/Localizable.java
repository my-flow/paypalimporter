package com.moneydance.modules.features.paypalimporter.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.text.StringSubstitutor;

import javax.annotation.Nullable;

/**
 * This i18n class provides language-dependent strings such as labels and
 * messages.
 */
public final class Localizable {

    private final ResourceBundle resourceBundle;

    Localizable(final String localizableResource, final Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle(
                localizableResource,
                locale);
    }

    public ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(
                this.resourceBundle.getBaseBundleName(),
                this.resourceBundle.getLocale());
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
    public URL getUrlHelp() throws MalformedURLException {
        return new URL(this.resourceBundle.getString("url_help"));
    }

    /**
     * @param currency Default currency that will be used.
     * @param currencies All currencies that were found for the PayPal account.
     * @return the question when multiple currencies are available
     */
    @Nullable public String getQuestionMessageMultipleCurrencies(
            final String currency,
            final List<String> currencies) {
        final String templateString = this.resourceBundle.getString(
                "question_message_multiple_currencies");

        Map<String, String> valuesMap =
                new ConcurrentHashMap<>(2);
        valuesMap.put("currency", currency);
        valuesMap.put("currencies", String.join(", ", currencies));
        StringSubstitutor sub = new StringSubstitutor(valuesMap);

        return sub.replace(templateString);
    }

    /**
     * @param errorCode PayPal-specific numeric error code
     * @return the user-friendly error message for a given error code or null
     *  if none is found
     */
    public Optional<String> getTranslatedErrorMessage(@Nullable final String errorCode) {
        try {
            final String key = String.format("error_message_%s", errorCode);
            return Optional.of(this.resourceBundle.getString(key));
        } catch (MissingResourceException e) {
            return Optional.empty();
        }
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
     * @param errorMessage Original error message that should be displayed
     * @return the error message when service call failed
     */
    @Nullable public String getErrorMessageServiceCallFailed(final String errorMessage) {
        final String templateString = this.resourceBundle.getString(
                "error_message_service_call_failed");

        Map<String, String> valuesMap =
                new ConcurrentHashMap<>(1);
        valuesMap.put("error.message", errorMessage);
        StringSubstitutor sub = new StringSubstitutor(valuesMap);

        return sub.replace(templateString);
    }

    /**
     * @return Error message when no connection could be established.
     */
    public String getErrorMessageConnectionFailed() {
        return this.resourceBundle.getString("error_message_connection_failed");
    }
}
