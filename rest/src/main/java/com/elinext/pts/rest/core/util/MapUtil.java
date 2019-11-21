package com.elinext.pts.rest.core.util;

import com.elinext.pts.rest.model.data.FilterType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MapUtil {

    private static final String MAP_REGEX = "[\\[\\]]";
    private static final String EMPTY_STRING = "";
    private static final String EQUAL_SIGN = "=";
    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";
    private static final String SPLIT_REGEX = "\\(|\\)";
    private static final String EMPTY_RECORD = "[]";

    private MapUtil() {
    }

    public static LinkedHashMap<FilterType, List<String>> parseFiltersIntoMap(String filters) {
        return Stream.of(filters)
                .map(b -> b.replaceAll(MAP_REGEX, EMPTY_STRING).split(SEMICOLON))
                .flatMap(Arrays::stream)
                .collect(LinkedHashMap::new, (map, x) -> map.put(FilterType.valueOf(x.split(EQUAL_SIGN)[0]), List.of(x.split(EQUAL_SIGN)[1].split(COMMA))), Map::putAll);
    }

    public static Map<String, String> parseObjectIntoMap(String object) {
        return Stream.of(object)
                .map(b -> b.split(SPLIT_REGEX)[1].split(COMMA))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(x -> x.split(EQUAL_SIGN)[0], e -> e.split(EQUAL_SIGN)[1]));
    }

    public static Map<String, String> parseDescriptionIntoMap(List<String> descriptions) {
        Map<String, String> storage = new HashMap<>();
        for (String description : descriptions) {
            if (!description.equalsIgnoreCase(EMPTY_RECORD)) {
                String[] values = description.replaceAll(MAP_REGEX, EMPTY_STRING).split(SEMICOLON);
                for (String value : values) {
                    storage.put(value.split(EQUAL_SIGN)[0].trim(), value.split(EQUAL_SIGN)[1]);
                }
            }
        }
        return storage;
    }
}
