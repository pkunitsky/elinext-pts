/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implements logic for date validation
 *
 * @author Natallia Paklonskaya
 */
public class DateValidator implements ConstraintValidator<AfterDate, DeadlineStartDate> {

    @Override
    public boolean isValid(DeadlineStartDate value, ConstraintValidatorContext context) {
        if (value.getStartDate() == null || value.getDeadline() == null) {
            return true;
        }
        return value.getDeadline().isAfter(value.getStartDate());
    }
}
