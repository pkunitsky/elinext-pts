/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.TimeReportConfigWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Endpoints for filtering and saving reports
 *
 * @author Natallia Paklonskaya
 */

@Api(value = "/configs", description = "Configuration of time report")
@RestController
@AllArgsConstructor
public class TimeReportConfigResource {

    private final TimeReportConfigWorkflow timeReportConfigWorkflow;

    @ApiOperation(value = "Creates a new filter.", response = TimeReportConfigDto.class)
    @PostMapping(value = "/configs/filter")
    public TimeReportConfigDto filter(@Validated @RequestBody TimeReportConfigDto timeReportConfigDto) {
        return timeReportConfigWorkflow.configureFilter(timeReportConfigDto);
    }

    @ApiOperation(value = "Saves filter settings.", response = TimeReportConfigDto.class)
    @PostMapping(value = "/configs")
    public TimeReportConfigDto saveFilter(@Validated @RequestBody TimeReportConfigDto timeReportConfigDto) {
        return timeReportConfigWorkflow.saveFilter(timeReportConfigDto);
    }

    @ApiOperation(value = "Finds filter by id.", response = TimeReportConfigDto.class)
    @GetMapping(value = "/configs")
    public TimeReportConfigDto getFilterSettings(@ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                                 @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return timeReportConfigWorkflow.fetchFilterSettings(userEmail, userRole);
    }

    @ApiOperation(value = "Delete filter by id.")
    @DeleteMapping(value = "/configs/{id}")
    public TimeReportConfigDto deleteFilterSettings(@PathVariable("id") Long configId) {
        return timeReportConfigWorkflow.deleteById(configId);
    }

    @ApiOperation(value = "Finds filter by name.", response = TimeReportConfigDto.class)
    @GetMapping(value = "/configs/saved/{name}")
    public TimeReportConfigDto getSavedFilterSettingsByName(@NotNull @PathVariable("name") String name) {
        return timeReportConfigWorkflow.fetchSavedFilterSettings(name);
    }
}
