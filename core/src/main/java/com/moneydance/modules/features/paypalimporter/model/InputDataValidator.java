// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2016 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.model;

import com.infinitekind.moneydance.model.DateRange;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.moneydance.modules.features.paypalimporter.util.Helper;
import com.moneydance.modules.features.paypalimporter.util.Localizable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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
        return StringUtils.isNotBlank(data.getUsername());
    }

    @SuppressWarnings("nullness")
    private static boolean isValidPassword(final InputData data) {
        return ArrayUtils.isNotEmpty(data.getPassword(false));
    }

    @SuppressWarnings("nullness")
    private static boolean isValidSignature(final InputData data) {
        return StringUtils.isNotBlank(data.getSignature());
    }

    private static boolean isValidDateRange(final InputData data) {
        final DateRange dateRange = data.getDateRange();
        return dateRange != null
                && dateRange.getStartDateInt()
                <= dateRange.getEndDateInt();
    }
}
