/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.dto;

import com.elinext.pts.rest.core.util.validator.DeadlineStartDate;
import com.elinext.pts.rest.model.entity.Status;
import com.elinext.pts.rest.core.util.validator.AfterDate;
import com.elinext.pts.rest.model.entity.Type;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Project dto class for transferring object state
 * between presentation and bl
 *
 * @author Natallia Paklonskaya
 */

@Builder
@AfterDate
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Project DTO")
public class ProjectDto implements DeadlineStartDate {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(notes = "The code is generated on the server.")
    private String code;

    @ApiModelProperty
    private String description;

    @ApiModelProperty
    private String projectLeadEmail;

    @ApiModelProperty
    private String managerEmail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean archived;

    @JsonFormat(pattern = DATE_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(notes = "Must be earlier than deadline.")
    private LocalDateTime startDate;

    @JsonFormat(pattern = DATE_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(notes = "Must be later than the start date.")
    private LocalDateTime deadline;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DATE_PATTERN)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime changedDate = LocalDateTime.now();

    @ApiModelProperty(allowableValues = "ACTIVE, SUBMITTED, IN_PLANNING, STARTED, ON_HOLD, COMPLETED, REJECTED")
    private Status status;

    @ApiModelProperty(notes = "Parent project.")
    private Long parentId;

    @ApiModelProperty
    private String customer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String groupName;

    @ApiModelProperty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> ids;

    @ApiModelProperty
    @JsonProperty
    private Type type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProjectDto> subprojects;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String message;

    @ApiModelProperty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<JoinedProjectDto> joinedProjects;
}

