/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.dto;

import com.elinext.pts.rest.model.entity.Status;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Time report dto class for transferring object state
 * between presentation and bl
 *
 * @author Natallia Paklonskaya
 */
@ApiModel(description = "Time report DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeReportDto {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private Long id;

    @NotNull(message = "task id is mandatory")
    @ApiModelProperty(required = true)
    private Long taskId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private String taskName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private String taskProjectName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private String reporterEmail;

    @NotNull(message = "'hours' field is mandatory")
    @ApiModelProperty(required = true)
    private Double hours;

    @NotNull(message = "reported date is mandatory")
    @JsonFormat(pattern = DATE_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(required = true)
    private LocalDateTime reportedDate;

    @NotNull(message = "report text is mandatory")
    @ApiModelProperty(required = true)
    private String comment;

    @Max(value = 100)
    @ApiModelProperty(allowableValues = "range[0, 100]")
    private Integer percentage;

    @ApiModelProperty(required = true, allowableValues = "ACTIVE, SUBMITTED, IN_PLANNING, STARTED, ON_HOLD, COMPLETED, REJECTED")
    private Status taskStatus;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String message;
}
