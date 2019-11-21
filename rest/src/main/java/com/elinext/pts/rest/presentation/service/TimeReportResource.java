/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.TimeReportWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * CRUD time report controller
 *
 * @author Natallia Paklonskaya
 */

@Api(value = "/reports", description = "Operations with reports.")
@RestController
@AllArgsConstructor
public class TimeReportResource {

    private final TimeReportWorkflow timeReportWorkflow;

    @ApiOperation(value = "Create new report.", response = TimeReportDto.class)
    @PostMapping(value = "/reports")
    public TimeReportDto add(@Validated @RequestBody TimeReportDto timeReportDto,
                             @ApiParam(required = false) @RequestHeader("Email") String userEmail) {
        return timeReportWorkflow.create(timeReportDto, userEmail);
    }

    @ApiOperation(value = "Find report by task id.", response = TimeReportDto.class)
    @GetMapping(value = "/reports/{taskId}")
    public Page<TimeReportDto> getReports(@PathVariable("taskId") Long taskId,
                                    Pageable pageable,
                                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                          @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return timeReportWorkflow.fetchByTaskId(taskId, pageable, userEmail, role);
    }


    @ApiOperation(value = "Return paginated time reports.", response = Page.class)
    @GetMapping(value = "/reports")
    public Page<TimeReportDto> getReports(Pageable pageable,
                                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                          @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return timeReportWorkflow.fetchTimeReports(pageable, userEmail, role);
    }

    @ApiOperation(value = "Delete time report by id.")
    @DeleteMapping(value = "/reports/{id}")
    public TimeReportDto deleteReport(@PathVariable("id") Long id,
                                      @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                      @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return timeReportWorkflow.deleteById(id, userEmail, role);
    }

    @ApiOperation(value = "Update time report by id.", response = TimeReportDto.class)
    @PutMapping(value = "/reports/{id}")
    public TimeReportDto updateReport(@PathVariable("id") Long id,
                                      @Validated @RequestBody TimeReportDto timeReportDto,
                                      @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                      @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return timeReportWorkflow.update(id, timeReportDto, userEmail, role);
    }

    @ApiOperation(value = "Returns paged reports based on the received filters and sorts.", response = TimeReportDto.class)
    @PostMapping(value = "/reports/run")
    public Page<TimeReportDto> runFilter(@Validated @RequestBody @ApiParam TimeReportConfigDto timeReportConfigDto,
                                         Pageable pageable,
                                         @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                         @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return timeReportWorkflow.runFilter(timeReportConfigDto, pageable, userEmail, role);
    }

    @ApiOperation(value = "Returns the last 5 reports for a user with the specified mail.", response = TimeReportDto.class, responseContainer = "List")
    @GetMapping(value = "/reports/user")
    public List<TimeReportDto> findTop5ByReporterId(@ApiParam(required = false) @RequestParam("email") String reporterEmail) {
        return timeReportWorkflow.findTop5ByReporterIdDesc(reporterEmail);
    }
}
