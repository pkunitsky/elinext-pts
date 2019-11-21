/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.TimeReport;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * The interface Excel report workflow.
 */
public interface ExcelReportWorkflow extends DefaultWorkflow<TimeReportDto, TimeReport>{
    /**
     * Create report file system resource.
     *
     * @param timeReports   the time reports
     * @param tableHeadName the table head name
     * @return the file system resource
     */
    FileSystemResource createReport(List<TimeReport> timeReports, String tableHeadName);

    /**
     * Fetch reports file system resource.
     *
     * @param timeReportConfigDto the time report config dto
     * @param role                the role
     * @param tableName           the table name
     * @return the file system resource
     * @throws AccessDeniedException the access denied exception
     */
    FileSystemResource fetchReports(TimeReportConfigDto timeReportConfigDto, Role role, String tableName) throws AccessDeniedException;

    /**
     * Fetch all reports file system resource.
     *
     * @param role      the role
     * @param tableName the table name
     * @return the file system resource
     * @throws AccessDeniedException the access denied exception
     */
    FileSystemResource fetchAllReports(Role role, String tableName) throws AccessDeniedException;
}