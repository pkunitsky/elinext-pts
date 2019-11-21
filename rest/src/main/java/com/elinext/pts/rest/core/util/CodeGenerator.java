/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util;

import com.elinext.pts.rest.model.entity.Project;

import static java.util.Objects.isNull;

/**
 * This is a utility class for generating code for entities
 *
 * @author Natallia Paklonskaya
 */

public final class CodeGenerator {
    private static final String ZERO = "0";
    private static final String TWO_ZERO = "00";
    private static final int NINE_FOR_GENERATOR_BOUNDARY = 9;
    private static final int NINETY_NINE_FOR_GENERATOR_BOUNDARY = 99;
    private static final String DASH = "-";

    private CodeGenerator() {
    }

    public static String getCode(Project project, int size) {
        String code = project.getCode();
        if (isNull(code)) {
            return generateCode(size);
        }
        return code;
    }

    private static String generateCode(int size) {
        String code = "P";
        if (size < NINE_FOR_GENERATOR_BOUNDARY) {
            code += TWO_ZERO;
        } else if (size < NINETY_NINE_FOR_GENERATOR_BOUNDARY) {
            code += ZERO;
        }
        return code + (++size);
    }

    public static String generateTaskCode(Long projectId, Long taskId) {
        String code = "P";
        if (projectId > 0) {
            if (projectId < 10) {
                code += TWO_ZERO;
            } else if (projectId < 100) {
                code += ZERO;
            }
        }
        return code + projectId + DASH + (taskId);
    }
}
