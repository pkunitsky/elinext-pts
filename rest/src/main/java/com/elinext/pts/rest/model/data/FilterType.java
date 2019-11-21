/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Natallia Paklonskaya
 */

public enum FilterType {
    @JsonProperty("project") PROJECT, @JsonProperty("project_status") PROJECT_STATUS, @JsonProperty("subproject") SUBPROJECT,
    @JsonProperty("task_status") TASK_STATUS, @JsonProperty("task") TASK, @JsonProperty("reporter") REPORTER, @JsonProperty("period") PERIOD
}
