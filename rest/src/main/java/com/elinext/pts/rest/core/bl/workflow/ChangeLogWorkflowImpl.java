package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.ChangeLogRepository;
import com.elinext.pts.rest.core.util.MapUtil;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.presentation.dto.ChangeLogDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ChangeLogWorkflowImpl implements ChangeLogWorkflow {

    private final ChangeLogRepository changeLogRepository;
    private final ModelMapper modelMapper;
    private final TaskWorkflow taskWorkflow;
    private final TimeReportWorkflow timeReportWorkflow;

    @Override
    public Page<ChangeLogDto> fetchAllByProjectId(Pageable pageable, Long projectId, String userEmail, Role role) {
        var tasks = getTasks(projectId, userEmail);
        var logs = copy(projectId, tasks, userEmail);
        List<ChangeLogDto> dtos = logs
                .stream()
                .map(e -> toDto(ChangeLogDto.class, e, modelMapper))
                .collect(Collectors.toList());
        dtos.forEach(e -> e.setValues(MapUtil.parseDescriptionIntoMap(logs
                .stream()
                .map(ChangeLog::getDescription)
                .collect(Collectors.toList()))));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    private List<ChangeLog> copy(Long projectId, Set<Task> tasks, String userEmail) {
        var projectChangeLogs = changeLogRepository.findByEntityIdAndEntityTypeInAndUserEmail(projectId, List.of(Type.PROJECT, Type.SUBPROJECT), userEmail);
        var taskLogs = getTaskChangeLog(tasks, userEmail);
        var reportLogs = getReportsChangeLog(tasks, userEmail);
        projectChangeLogs.addAll(taskLogs);
        projectChangeLogs.addAll(reportLogs);
        return projectChangeLogs;
    }

    private List<ChangeLog> getTaskChangeLog(Set<Task> task, String userEmail) {
        List<Long> ids = task.stream().map(Task::getId).collect(Collectors.toList());
        return changeLogRepository.findByEntityIdInAndEntityTypeInAndUserEmail(ids, List.of(Type.TASK, Type.SUBTASK), userEmail);
    }

    private Set<Task> getTasks(Long projectId, String userEmail) {
        return taskWorkflow.findByProjectIdAndGroupMember(projectId, userEmail);
    }

    private List<Long> getTaskIds(Set<Task> tasks) {
        return tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }

    private List<ChangeLog> getReportsChangeLog(Set<Task> tasks, String userEmail) {
        List<Long> ids = getTaskIds(tasks);
        List<Long> reportIds = timeReportWorkflow.findAllByTaskIdsAndGroupMembers(ids, userEmail)
                .stream()
                .map(TimeReport::getId)
                .collect(Collectors.toList());
        return changeLogRepository.findByEntityIdInAndEntityTypeInAndUserEmail(reportIds, List.of(Type.TIME_REPORT), userEmail);
    }
}
