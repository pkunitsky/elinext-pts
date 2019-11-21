package com.elinext.pts.rest.presentation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationDto {

    private String status;

    private String message;

}
