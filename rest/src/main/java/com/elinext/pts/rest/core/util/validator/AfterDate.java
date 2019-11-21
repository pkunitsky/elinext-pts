/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The interface After Date. Performs validation to be sure one date after another.
 *
 * @author Natallia Paklonskaya
 */

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DateValidator.class})
public @interface AfterDate {
    String message() default "The deadline should be after the starting date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
