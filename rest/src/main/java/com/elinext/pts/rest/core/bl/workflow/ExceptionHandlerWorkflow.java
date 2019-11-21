/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.presentation.dto.ValidationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Basic interface for exception handler controller advice
 *
 * @author Natallia Paklonskaya
 */

public interface ExceptionHandlerWorkflow {

    String DEFAULT_MASSAGE = "Validation error";

    /**
     * handles custom validation constraint
     *
     * @return {@link ResponseEntity} with bad request status code
     * and validation message in body
     */
    default ResponseEntity handleCustomErrors(BindingResult exResult) {
        List<ObjectError> errors = exResult.getAllErrors();
        List<ValidationDto> validationList = new ArrayList<>();
        if (!errors.isEmpty()) {
            validationList = errors.stream().filter(Objects::nonNull)
                    .map(e -> ValidationDto.builder()
                            .status(HttpStatus.BAD_REQUEST.toString())
                            .message(e.getDefaultMessage())
                            .build()).collect(Collectors.toList());
        }
        return ResponseEntity.badRequest().body(validationList);
    }

    /**
     * handles default validation constraint
     *
     * @return {@link ResponseEntity} with bad request status code
     * and validation message in body
     */
    default ResponseEntity handleFieldErrors(FieldError fieldError) {
        ValidationDto validationDto = ValidationDto.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(fieldError != null ? fieldError.getField() + ": " +fieldError.getDefaultMessage() : DEFAULT_MASSAGE)
                .build();
        return ResponseEntity.badRequest().body(validationDto);
    }
}
