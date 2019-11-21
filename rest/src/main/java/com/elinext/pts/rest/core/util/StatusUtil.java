/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util;

import com.elinext.pts.rest.model.entity.Status;
import com.elinext.pts.rest.model.reference.FilterCriteria;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a util class for mapping statuses from string to enum
 *
 * @author Natallia Paklonskaya
 */

public final class StatusUtil {

    private StatusUtil() {
    }

    public static List<String> getStatuses() {
        return Stream.of(Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static List<Status> getStatuses(FilterCriteria criteria) {
        return criteria.getFilterValues()
                .stream()
                .map(Status::valueOf)
                .collect(Collectors.toList());
    }
}
