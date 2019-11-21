/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortByType {

    @JsonProperty("date") DATE("reportedDate"), @JsonProperty("email") EMAIL("reporterEmail"), @JsonProperty("projectName") PROJECT("projectReportName"),
    @JsonProperty("taskName") TASK("taskName"), @JsonProperty("hours") HOURS("hours");

    private final String name;

    SortByType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
