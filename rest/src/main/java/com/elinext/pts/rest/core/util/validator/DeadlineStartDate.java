/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.util.validator;

import java.time.LocalDateTime;

/**
 * @author Natallia Paklonskaya
 */

public interface DeadlineStartDate {
    /**
     * Returns start date for dto
     *
     * @return {@link LocalDateTime}
     */
    LocalDateTime getStartDate();

    /**
     * Returns deadline for dto
     *
     * @return {@link LocalDateTime}
     */
    LocalDateTime getDeadline();
}
