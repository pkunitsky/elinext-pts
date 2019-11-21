/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.ExcelReportWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

@RestController
@AllArgsConstructor
@Api(value = "/excel", description = "Export reports to excel")
@RequestMapping("excel")
public class ExcelReportResource {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String ATTACHMENT_FILENAME = "attachment; filename=%s";
    private final ExcelReportWorkflow excelReportWorkflow;

    @GetMapping
    @ApiOperation(value = "Returns all reports in the database.")
    public ResponseEntity getAllReports(HttpServletResponse response, @RequestParam String tableName,
                                        @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        userRole = Role.ADMIN;
        ResponseEntity responseEntity = null;
        try {
            File file = new File(excelReportWorkflow.fetchAllReports(userRole, tableName).getPath());
            responseEntity = ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.valueOf(XLSX_CONTENT_TYPE))
                    .cacheControl(CacheControl.noCache())
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format(ATTACHMENT_FILENAME, file.getName()))
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (AccessDeniedException e) {
            LOGGER.log(Level.ERROR, "Not enough rights: " + e);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.ERROR, "File not found: " + e);
        }
        return responseEntity;
    }

    @PostMapping("filtered")
    @ApiOperation(value = "Returns filtered reports by TimeReportConfig.")
    public ResponseEntity fetchReports(@RequestBody @ApiParam TimeReportConfigDto timeReportConfigDto, HttpServletResponse response,
                                           @RequestParam String tableName, @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        ResponseEntity responseEntity = null;
        try {
            File file = new File(excelReportWorkflow.fetchReports(timeReportConfigDto, userRole, tableName).getPath());
            responseEntity = ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.valueOf(XLSX_CONTENT_TYPE))
                    .cacheControl(CacheControl.noCache())
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format(ATTACHMENT_FILENAME, file.getName()))
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (AccessDeniedException e) {
            LOGGER.log(Level.ERROR, "Not enough rights: " + e);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.ERROR, "File not found: " + e);
        }
        return responseEntity;
    }
}
