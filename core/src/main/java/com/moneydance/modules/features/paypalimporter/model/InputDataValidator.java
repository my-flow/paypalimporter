package com.moneydance.modules.features.paypalimporter.model;

import com.jgoodies.common.internal.ResourceBundleAccessor;
import com.jgoodies.common.internal.StringResourceAccessor;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class InputDataValidator implements Validator<InputData> {

    private final Localizable localizable;
    private final StringResourceAccessor localizer;

    public enum MessageKey {
        USERNAME,
        PASSWORD,
        SIGNATURE,
        DATERANGE;
    }

    public InputDataValidator(final Localizable argLocalizable) {
        this.localizable = argLocalizable;
        this.localizer = new ResourceBundleAccessor(argLocalizable.getResourceBundle());
    }

    @Override
    public ValidationResult validate(final InputData data)  {
        final ValidationResult result = new ValidationResult();
        if (!isValidUsername(data)) {
            result.addError(
                    this.localizable.getErrorMessageUsernameBlank(),
                    MessageKey.USERNAME);
        }
        if (!isValidPassword(data)) {
            result.addError(
                    this.localizable.getErrorMessagePasswordBlank(),
                    MessageKey.PASSWORD);
        }
        if (!isValidSignature(data)) {
            result.addError(
                    this.localizable.getErrorMessageSignatureBlank(),
                    MessageKey.SIGNATURE);
        }
        if (!isValidDateRange(data)) {
            result.addError(this.localizable
                    .getErrorMessageStartDateNotBeforeEndDate(),
                    MessageKey.DATERANGE);
        }
        return ValidationResult.unmodifiableResult(result);
    }

    @SuppressWarnings("nullness")
    private boolean isValidUsername(final InputData data) {
        final Optional<String> username = data.getUsername();
        return username.isPresent() && StringUtils.isNotBlank(username.get())
                && !username.get().equals(this.localizer.getString("hint_username"));
    }

    @SuppressWarnings("nullness")
    private static boolean isValidPassword(final InputData data) {
        AtomicBoolean result = new AtomicBoolean(false);
        data.getPassword(false).ifPresent(password -> result.set(ArrayUtils.isNotEmpty(password)));
        return result.get();
    }

    @SuppressWarnings("nullness")
    private boolean isValidSignature(final InputData data) {
        final Optional<String> signature = data.getSignature();
        return signature.isPresent() && StringUtils.isNotBlank(signature.get())
                && !signature.get().equals(this.localizer.getString("hint_signature"));
    }

    private static boolean isValidDateRange(final InputData data) {
        return data
                .getDateRange()
                .filter(dateRange -> dateRange.getStartDateInt() <= dateRange.getEndDateInt())
                .isPresent();
    }
}
