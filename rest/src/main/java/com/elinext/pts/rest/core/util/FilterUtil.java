/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util;

import com.elinext.pts.rest.model.entity.SortOrderType;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * This is a util class for filtering entities
 *
 * @author Natallia Paklonskaya
 */

public final class FilterUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String EMPTY_STRING = "";
    private static final String SEMICOLON = ";";
    private static final String PREFIX = "[";
    private static final String SUFFIX = "]";

    private FilterUtil() {
    }

    public static List<FilterCriteria> getFilterCriteria(TimeReportConfigDto timeReportConfigDto) {
        return nonNull(timeReportConfigDto.getFilters()) ? timeReportConfigDto.getFilters().entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(e -> new FilterCriteria(e.getKey(), e.getValue()))
                .collect(Collectors.toList()) : List.of();
    }

    public static String getFilters(TimeReportConfigDto timeReportConfigDto) {
        return nonNull(timeReportConfigDto.getFilters()) ? timeReportConfigDto.getFilters().entrySet()
                .stream()
                .map(Map.Entry::toString)
                .collect(Collectors.joining(SEMICOLON, PREFIX, SUFFIX)) : EMPTY_STRING;
    }

    public static Sort getSortingSettings(TimeReportConfigDto timeReportConfigDto) {
        String sortByName = timeReportConfigDto.getSortBy().getName();
        return timeReportConfigDto.getSortOrder() == SortOrderType.ASC ?
                Sort.by(sortByName).ascending() :
                Sort.by(sortByName).descending();
    }

    public static List<LocalDateTime> getDate(FilterCriteria criteria) {
        return criteria.getFilterValues()
                .stream()
                .map(e -> LocalDateTime.parse(e, DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .collect(Collectors.toList());
    }
}
