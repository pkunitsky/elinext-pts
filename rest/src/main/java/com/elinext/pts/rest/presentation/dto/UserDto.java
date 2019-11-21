package com.elinext.pts.rest.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "User DTO")
public class UserDto {

    @ApiModelProperty
    private Long id;

    @ApiModelProperty(required = true)
    private String email;

    @ApiModelProperty
    private String firstName;

    @ApiModelProperty
    private String lastName;
}
