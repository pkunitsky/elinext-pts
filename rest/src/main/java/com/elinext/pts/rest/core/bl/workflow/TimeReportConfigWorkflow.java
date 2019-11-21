/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.TimeReportConfiguration;
import com.elinext.pts.rest.model.exception.EntityAlreadyExistsException;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;


/**
 * The interface Time Report Configuration Workflow. Performs the business logic for time report configuration entity.
 *
 * @author Natallia Paklonskaya
 */
public interface TimeReportConfigWorkflow extends DefaultWorkflow<TimeReportConfigDto, TimeReportConfiguration> {

    /**
     * Configures filter setting and returns number of records
     *
     * @return {@link TimeReportConfigDto} as response body
     */
    TimeReportConfigDto configureFilter(TimeReportConfigDto timeReportConfigDto);

    /**
     * Saves the filter
     *
     * @return {@link TimeReportConfigDto} as response body
     * @throws {@link EntityAlreadyExistsException}
     */
    TimeReportConfigDto saveFilter(TimeReportConfigDto timeReportConfigDto);

    /**
     * Fetches filter settings and provides available setting for certain user
     *
     * @return {@link TimeReportConfigDto} as response body
     */
    TimeReportConfigDto fetchFilterSettings(String userEmail, Role role);

    /**
     * @param name of saved config
     * @return {@link TimeReportConfigDto} as response body
     */
    TimeReportConfigDto fetchSavedFilterSettings(String name);

    /**
     * @param id time report configuration to be deleted
     */
    TimeReportConfigDto deleteById(Long id);
}
