/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.ExceptionHandlerWorkflow;
import com.elinext.pts.rest.model.exception.EntityAlreadyExistsException;
import com.elinext.pts.rest.model.exception.EntityNotFoundException;

import com.elinext.pts.rest.model.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;


/**
 * controller advice for handling exceptions.
 *
 * @author Natallia Paklonskaya
 */

@ControllerAdvice
@Slf4j
public class ResourceAdvice implements ExceptionHandlerWorkflow {

    @ExceptionHandler(EntityNotFoundException.class)
    public void handleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidation(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public void handleAlreadyExists(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity handleConstraintViolationException(MethodArgumentNotValidException ex) {
        BindingResult exResult = ex.getBindingResult();
        FieldError fieldError = exResult.getFieldError();
        return nonNull(fieldError) ? handleFieldErrors(fieldError) : handleCustomErrors(exResult);
    }
}
