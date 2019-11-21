/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.dto;

import com.elinext.pts.rest.model.data.FilterType;
import com.elinext.pts.rest.model.entity.SortByType;
import com.elinext.pts.rest.model.entity.SortOrderType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Time Report dto class for transferring object state
 * between presentation and bl
 *
 * @author Natallia Paklonskaya
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Time report config DTO")
public class TimeReportConfigDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ApiModelProperty
    private LinkedHashMap<FilterType, List<String>> filters;

    @NotNull
    @ApiModelProperty
    private SortByType sortBy;

    @NotNull
    @ApiModelProperty
    private SortOrderType sortOrder;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private List<SortOrderType> sortOrderTypes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private List<SortByType> sortByTypes;

    @ApiModelProperty
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty
    private Integer numberOfFilteredRecords;
}
