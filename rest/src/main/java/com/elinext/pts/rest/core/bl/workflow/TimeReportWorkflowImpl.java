/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.bl.appservice.TimeReportConfigSpecs;
import com.elinext.pts.rest.core.dal.repository.TimeReportRepository;
import com.elinext.pts.rest.core.util.FilterUtil;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TimeReportWorkflow}
 *
 * @author Natallia Paklonskaya
 */

@Service
@Slf4j
@AllArgsConstructor
public class TimeReportWorkflowImpl implements TimeReportWorkflow {

    private static final String FORBIDDEN = "Forbidden";

    private final ModelMapper modelMapper;
    private final TimeReportRepository timeReportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserWorkflow userWorkflow;
    private final TaskWorkflow taskWorkflow;

    @Override
    public TimeReportDto create(TimeReportDto timeReportDto, String userEmail) {
        var task = taskWorkflow.findByProjectGroupsMember(timeReportDto.getTaskId(), userEmail);
        if (task.isPresent()) {
            return create(userEmail, timeReportDto, task.get());
        }
        return TimeReportDto.builder().message(FORBIDDEN).build();
    }

    private TimeReportDto create(String userEmail, TimeReportDto timeReportDto, Task task) {
        var reporter = getUserByEmail(userEmail, userWorkflow);
        var timeReport = toEntity(timeReportDto, TimeReport.class, modelMapper);
        timeReport.setReporter(reporter);
        updateTask(task, timeReportDto, timeReport);
        timeReport = timeReportRepository.save(timeReport);
        var fields = fetchFields(timeReport);
        publish(fields, reporter, timeReport.getId(), Type.TIME_REPORT, EventType.CREATED, applicationEventPublisher);
        return toDto(TimeReportDto.class, timeReport, modelMapper);
    }

    @Override
    public Page<TimeReportDto> fetchTimeReports(Pageable pageable, String userEmail, Role role) {
        var timeReports = isAdmin(role) ?
                timeReportRepository.findAll(pageable) :
                timeReportRepository.findByReporterGroupUsersEmail(pageable, userEmail);
        return timeReports.map(e -> this.toDto(TimeReportDto.class, e, modelMapper));
    }

    @Override
    public List<TimeReport> fetchTotalTimeReports() {
        return timeReportRepository.findAll();
    }

    @Override
    public Page<TimeReportDto> fetchByTaskId(Long taskId, Pageable pageable, String userEmail, Role role) {
        var timeReports = isAdmin(role) ?
                timeReportRepository.findByTaskId(taskId, pageable) :
                timeReportRepository.findByTaskIdAndReporterGroupUsersEmail(pageable, taskId, userEmail);
        return timeReports.map(e -> this.toDto(TimeReportDto.class, e, modelMapper));
    }

    @Override
    public Page<TimeReportDto> runFilter(TimeReportConfigDto timeReportConfigDto, Pageable pageable, String userEmail, Role role) {
        List<FilterCriteria> filterCriteria = FilterUtil.getFilterCriteria(timeReportConfigDto);
        Sort sort = FilterUtil.getSortingSettings(timeReportConfigDto);
        Page<TimeReport> timeReports = isAdmin(role) ?
                timeReportRepository.findAll(TimeReportConfigSpecs.findByCriteria(filterCriteria), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort)) :
                timeReportRepository.findByReporterGroupUsersEmail(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort), userEmail);
        return timeReports.map(e -> this.toDto(TimeReportDto.class, e, modelMapper));
    }

    @Override
    public TimeReportDto deleteById(Long id, String userEmail, Role role) {
        var timeReport = isAdmin(role) ?
                timeReportRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.TIME_REPORT)) :
                timeReportRepository.findByIdAndReporterEmailOrTaskManagerEmail(id, userEmail, userEmail).orElseThrow(supplyEntityNotFoundException(id, Type.TIME_REPORT));
        timeReportRepository.delete(timeReport);
        return toDto(TimeReportDto.class, timeReport, modelMapper);
    }

    @Override
    public TimeReportDto update(Long id, TimeReportDto timeReportDto, String userEmail, Role role) {/**/
        var creator = getUserByEmail(userEmail, userWorkflow);
        var timeReport = isAdmin(role) ?
                timeReportRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.TIME_REPORT)) :
                timeReportRepository.findByIdAndReporterEmailOrTaskManagerEmail(id, userEmail, userEmail).orElseThrow(supplyEntityNotFoundException(id, Type.TIME_REPORT));
        var timeReportForCompare = getUpdatedTimeReport(timeReportDto, timeReport.getId(), modelMapper, creator);
        var fields = fetchEntityDifference(timeReport, timeReportForCompare);
        if (isDifferentFields(fields)) {
            publish(fields, creator, timeReport.getId(), Type.TIME_REPORT, EventType.EDITED, applicationEventPublisher);
            mergeUpdatedAndCurrentEntity(timeReportForCompare, timeReport, modelMapper);
            timeReport = timeReportRepository.save(timeReport);
        }
        return toDto(TimeReportDto.class, timeReport, modelMapper);
    }

    @Override
    public List<TimeReportDto> findTop5ByReporterIdDesc(String reporterEmail) {
        var userReports = timeReportRepository.findTop5ByReporter_EmailOrderByReportedDateDesc(reporterEmail);
        return userReports.size() == 0 ? new ArrayList<>() :
                userReports.stream().map(report -> toDto(TimeReportDto.class, report, modelMapper)).collect(Collectors.toList());
    }

    @Override
    public Set<TimeReport> findAllByTaskIdsAndGroupMembers(List<Long> ids, String userEmail) {
        return timeReportRepository.findByTaskIdInAndTaskProjectGroupsUsersEmail(ids, userEmail);
    }
}
