/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.exception;

/**
 * General exception when validation error occurs
 *
 * @author Natallia Paklonskaya
 */

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}

