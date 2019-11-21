/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.bl.appservice.TimeReportConfigSpecs;
import com.elinext.pts.rest.core.dal.repository.*;
import com.elinext.pts.rest.core.util.FilterUtil;
import com.elinext.pts.rest.core.util.MapUtil;
import com.elinext.pts.rest.core.util.StatusUtil;
import com.elinext.pts.rest.model.data.FilterType;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.TimeReport;
import com.elinext.pts.rest.model.entity.TimeReportConfiguration;
import com.elinext.pts.rest.model.entity.Type;
import com.elinext.pts.rest.model.exception.EntityAlreadyExistsException;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.elinext.pts.rest.model.entity.SortByType.*;
import static com.elinext.pts.rest.model.entity.SortOrderType.ASC;
import static com.elinext.pts.rest.model.entity.SortOrderType.DESC;


/**
 * Implementation of {@link TimeReportConfigWorkflow}
 *
 * @author Natallia Paklonskaya
 */

@Service
@Slf4j
@AllArgsConstructor
public class TimeReportConfigWorkflowImpl implements TimeReportConfigWorkflow {

    private final TimeReportRepository timeReportRepository;
    private final TimeReportConfigRepository timeReportConfigRepository;
    private final ModelMapper modelMapper;
    private final ProjectWorkflow projectWorkflow;
    private final TaskWorkflow taskWorkflow;
    private final UserWorkflow userWorkflow;

    @Override
    public TimeReportConfigDto configureFilter(TimeReportConfigDto timeReportConfigDto) {
        List<FilterCriteria> filterCriteria = FilterUtil.getFilterCriteria(timeReportConfigDto);
        if (CollectionUtils.isNotEmpty(filterCriteria)) {
            List<TimeReport> timeReports = timeReportRepository.findAll(TimeReportConfigSpecs.findByCriteria(filterCriteria));
            timeReportConfigDto.setNumberOfFilteredRecords(timeReports.size());
        }
        return timeReportConfigDto;
    }

    @Override
    public TimeReportConfigDto saveFilter(TimeReportConfigDto timeReportConfigDto) {
        TimeReportConfiguration timeReportConfiguration = timeReportConfigRepository.findByName(timeReportConfigDto.getName());
        if (timeReportConfiguration != null) {
            throw new EntityAlreadyExistsException(timeReportConfigDto.getName(), Type.TIME_REPORT_CONFIGURATION);
        }
        String filters = FilterUtil.getFilters(timeReportConfigDto);
        timeReportConfiguration = toEntity(timeReportConfigDto, TimeReportConfiguration.class, modelMapper);
        timeReportConfiguration.setSelectedFilters(filters);
        timeReportConfiguration = timeReportConfigRepository.save(timeReportConfiguration);
        timeReportConfigDto = toDto(TimeReportConfigDto.class, timeReportConfiguration, modelMapper);
        setFilters(timeReportConfiguration.getSelectedFilters(), timeReportConfigDto);
        return timeReportConfigDto;
    }

    @Override
    public TimeReportConfigDto fetchFilterSettings(String userEmail, Role role) {
        LinkedHashMap<FilterType, List<String>> filters = fillInFilters(userEmail, role);
        TimeReportConfigDto timeReportConfigDto = new TimeReportConfigDto();
        timeReportConfigDto.setFilters(filters);
        timeReportConfigDto.setSortByTypes(List.of(DATE, EMAIL, PROJECT, TASK, HOURS));
        timeReportConfigDto.setSortOrderTypes(List.of(ASC, DESC));
        return timeReportConfigDto;
    }

    @Override
    public TimeReportConfigDto fetchSavedFilterSettings(String name) {
        TimeReportConfiguration timeReportConfiguration = timeReportConfigRepository.findByName(name);
        TimeReportConfigDto timeReportConfigDto = toDto(TimeReportConfigDto.class, timeReportConfiguration, modelMapper);
        setFilters(timeReportConfiguration.getSelectedFilters(), timeReportConfigDto);
        return timeReportConfigDto;
    }

    @Override
    public TimeReportConfigDto deleteById(Long id) {
        TimeReportConfiguration timeReportConfiguration = timeReportConfigRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.TIME_REPORT_CONFIGURATION));
        timeReportConfigRepository.delete(timeReportConfiguration);
        return toDto(TimeReportConfigDto.class, timeReportConfiguration, modelMapper);
    }

    private LinkedHashMap<FilterType, List<String>> fillInFilters(String userEmail, Role role) {
        LinkedHashMap<FilterType, List<String>> filters = new LinkedHashMap<>();
        filters.put(FilterType.PROJECT_STATUS, StatusUtil.getStatuses());
        filters.put(FilterType.TASK_STATUS, StatusUtil.getStatuses());
        filters.put(FilterType.PROJECT, projectWorkflow.getProjectsName(userEmail, role));
        filters.put(FilterType.TASK, taskWorkflow.getTasksName(userEmail, role));
        filters.put(FilterType.SUBPROJECT, projectWorkflow.getSubprojectsName(userEmail, role));
        filters.put(FilterType.REPORTER, userWorkflow.getEmails());
        return filters;
    }

    private void setFilters(String filters, TimeReportConfigDto timeReportConfigDto) {
        if (StringUtils.isNotEmpty(filters)) {
            LinkedHashMap<FilterType, List<String>> filterTypeListMap = MapUtil.parseFiltersIntoMap(filters);
            timeReportConfigDto.setFilters(filterTypeListMap);
        }
    }
}
