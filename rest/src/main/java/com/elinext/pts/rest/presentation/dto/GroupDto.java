/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Group DTO")
public class GroupDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private Long id;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(required = true, notes = "Group name")
    private String name;

    private Set<UserDto> users;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<UserDto> availableUsers;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String message;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> projectIds;
}
