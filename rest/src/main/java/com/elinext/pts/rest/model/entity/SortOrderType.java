/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortOrderType {
    @JsonProperty("asc") ASC, @JsonProperty("desc") DESC
}
