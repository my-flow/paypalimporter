/*
 * PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
 * Copyright (C) 2013 Florian J. Breunig. All rights reserved.
 */

package com.moneydance.modules.features.paypalimporter.model;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

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

    public InputDataValidator() {
        this.localizable = Helper.INSTANCE.getLocalizable();
    }

    @Override
    public ValidationResult validate(final InputData data)  {
        final ValidationResult result = new ValidationResult();
        if (!validateUsername(data)) {
            result.addError(
                    this.localizable.getErrorMessageUsernameBlank(),
                    MessageKey.USERNAME);
        }
        if (!validatePassword(data)) {
            result.addError(
                    this.localizable.getErrorMessagePasswordBlank(),
                    MessageKey.PASSWORD);
        }
        if (!validateSignature(data)) {
            result.addError(
                    this.localizable.getErrorMessageSignatureBlank(),
                    MessageKey.SIGNATURE);
        }
        if (!validateDateRange(data)) {
            result.addError(this.localizable
                    .getErrorMessageStartDateNotBeforeEndDate(),
                    MessageKey.DATERANGE);
        }
        return ValidationResult.unmodifiableResult(result);
    }

    private static boolean validateUsername(final InputData data) {
        return StringUtils.isNotBlank(data.getUsername());
    }

    private static boolean validatePassword(final InputData data) {
        return ArrayUtils.isNotEmpty(data.getPassword(false));
    }

    private static boolean validateSignature(final InputData data) {
        return StringUtils.isNotBlank(data.getSignature());
    }

    private static boolean validateDateRange(final InputData data) {
        return data.getDateRange() != null
                && data.getDateRange().getStartDateInt()
                <= data.getDateRange().getEndDateInt();
    }
}
