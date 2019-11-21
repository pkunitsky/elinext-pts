/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.bl.appservice.EntityUpdatedEventListener;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;

/**
 * The interface Project Workflow. Performs the business logic for project entity.
 *
 * @author Natallia Paklonskaya
 */

public interface TimeReportWorkflow extends DefaultWorkflow<TimeReportDto, TimeReport> {

    /**
     * Creates new time report entity and triggers event listener {@link EntityUpdatedEventListener}
     * for creating change log record
     * <p>
     * Required params for time report creation:
     * taskId, reportedDate, hours, reporter, comment
     *
     * @param timeReportDto request body
     */
    TimeReportDto create(TimeReportDto timeReportDto, String userEmail);

    /**
     * Fetches page interface with time report dto
     *
     * @param pageable {@link Pageable} interface for pagination information
     * @return list of project dto
     */
    Page<TimeReportDto> fetchTimeReports(Pageable pageable, String userEmail, Role role);

    /**
     * Fetches all reports for statistics
     *
     * @return list of project dto
     */
    List<TimeReport> fetchTotalTimeReports();

    /**
     * Fetches a requested time report by id
     *
     * @param id time report id
     * @return time report dto as response body
     */
    Page<TimeReportDto> fetchByTaskId(Long id, Pageable pageable, String userEmail, Role role);

    /**
     * Deletes a time report record by id
     *
     * @param id time report id
     */
    TimeReportDto deleteById(Long id, String userEmail, Role role);

    /**
     * Updates a record if it presents
     *
     * @param timeReportDto request body
     * @param id            time report id
     * @return time report dto as response body
     */
    TimeReportDto update(Long id, TimeReportDto timeReportDto, String userEmail, Role role);

    /**
     * Runs a filter
     *
     * @return {@link TimeReportDto}
     */
    Page<TimeReportDto> runFilter(TimeReportConfigDto timeReportConfigDto, Pageable pageable, String userEmail, Role role);

    /**
     * Fetches the last five records
     *
     * @return list of {@link TimeReportDto}
     */
    List<TimeReportDto> findTop5ByReporterIdDesc(String reporterEmail);


    /**
     * Fetches the records by task id and group member
     *
     * @return set of {@link TimeReportDto}
     */
    Set<TimeReport> findAllByTaskIdsAndGroupMembers(List<Long> ids, String userEmail);

    default void updateTask(Task task, TimeReportDto timeReportDto, TimeReport timeReport) {
        Status status = timeReportDto.getTaskStatus();
        if (nonNull(status)) {
            task.setStatus(status);
            timeReport.setTask(task);
        }
    }

    default TimeReport getUpdatedTimeReport(TimeReportDto timeReportDto, Long id, ModelMapper modelMapper, User reporter) {
        timeReportDto.setId(id);
        TimeReport timeReport = toEntity(timeReportDto, TimeReport.class, modelMapper);
        timeReport.setReporter(reporter);
        return timeReport;
    }
}
