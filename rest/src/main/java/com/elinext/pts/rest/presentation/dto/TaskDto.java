package com.elinext.pts.rest.presentation.dto;

import com.elinext.pts.rest.core.util.validator.DeadlineStartDate;
import com.elinext.pts.rest.model.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * The type Task dto.
 *
 * @author Denis Senchenko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Task DTO")
public class TaskDto implements DeadlineStartDate {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private Long id;

    @ApiModelProperty(allowableValues = "NORMAL, LOW, ASAP, HIGH")
    private Priority priority = Priority.NORMAL;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(required = true)
    private String name;

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

    @JsonFormat(pattern = DATE_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(notes = "It is installed and changed on the server.")
    private LocalDateTime changedDate;

    private Double estimatedTime;

    @ApiModelProperty(allowableValues = "ACTIVE, SUBMITTED, IN_PLANNING, STARTED, ON_HOLD, COMPLETED, REJECTED")
    private Status status;

    @ApiModelProperty(notes = "Parent task.")
    private String parentId;
    
    @ApiModelProperty(notes = "The code is generated on the server.")
    private String code;

    @ApiModelProperty(required = true)
    private String description;

    @Max(value = 100)
    @ApiModelProperty(allowableValues = "range[0, 100]", notes = "The default value is 0.")
    private Integer percentage;

    @ApiModelProperty
    private Boolean archived;

    @NotNull(message = "project id is mandatory")
    private Long projectId;

    private String assigneeEmail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Page<TimeReportDto> timeReports;

    private Double totalHours;

    private Integer sizeOfReports;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<TaskDto> subtasks;

    @ApiModelProperty(allowableValues= "project, subproject, TASK, FEATURE, SUBTASK, TIME_REPORT, TIME_REPORT_CONFIGURATION, GROUP",
            notes = "If the field is not specified and the parent task too, then the TASK type will be set. If you specify a parent, the SUBTASK will be set.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Type type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> ids;
}
