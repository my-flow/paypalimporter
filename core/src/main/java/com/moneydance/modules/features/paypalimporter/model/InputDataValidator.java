// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2019 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Florian J. Breunig
 */
public final class InputDataValidator implements Validator<InputData> {

    private final Localizable localizable;

    /**
     * @author Florian J. Breunig
     */
    public enum MessageKey {
        USERNAME,
        PASSWORD,
        SIGNATURE,
        DATERANGE;
    }

    public InputDataValidator(final Localizable argLocalizable) {
        this.localizable = argLocalizable;
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
    private static boolean isValidUsername(final InputData data) {
        return data.getUsername().isPresent() && StringUtils.isNotBlank(data.getUsername().get());
    }

    @SuppressWarnings("nullness")
    private static boolean isValidPassword(final InputData data) {
        AtomicBoolean result = new AtomicBoolean(false);
        data.getPassword(false).ifPresent(password -> result.set(ArrayUtils.isNotEmpty(password)));
        return result.get();
    }

    @SuppressWarnings("nullness")
    private static boolean isValidSignature(final InputData data) {
        return data.getSignature().isPresent() && StringUtils.isNotBlank(data.getSignature().get());
    }

    private static boolean isValidDateRange(final InputData data) {
        return data
                .getDateRange()
                .filter(dateRange -> dateRange.getStartDateInt() <= dateRange.getEndDateInt())
                .isPresent();
    }
}
