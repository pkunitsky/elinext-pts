/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.TaskRepository;
import com.elinext.pts.rest.core.util.CodeGenerator;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import com.elinext.pts.rest.presentation.dto.TaskDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * The type Task workflow.
 *
 * @author Denis Senchenko
 */
@Service
@AllArgsConstructor
public class TaskWorkflowImpl implements TaskWorkflow {

    private static final String DEFAULT_VERSION = "1.%s";
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserWorkflow userWorkflow;
    private final ProjectWorkflow projectWorkflow;

    @Override
    public TaskDto fetchByTaskId(Long taskId, String userEmail, Role role) {
        Task task = isAdmin(role) ? taskRepository.findById(taskId).orElseThrow(supplyEntityNotFoundException(taskId, Type.TASK)) :
                taskRepository.findByIdAndProjectGroupsUsersEmail(taskId, userEmail).orElseThrow(supplyEntityNotFoundException(taskId, Type.TASK));
        return toDto(TaskDto.class, task, modelMapper);
    }

    @Override
    public TaskDto getTaskByCode(String code) {
        Task task = taskRepository.findByCode(code);
        return this.toDto(TaskDto.class, task, modelMapper);
    }

    @Override
    public TaskDto create(TaskDto taskDto, String userEmail, Role role) {
        Optional<Project> project = projectWorkflow.findByGroupMembers(taskDto.getProjectId(), userEmail);
        if(isAdmin(role) || project.isPresent()){
            Task task = this.toEntity(taskDto, Task.class, modelMapper);
            User creator = getUserByEmail(userEmail, userWorkflow);
            fillInTask(taskDto, task, creator);
            fillTypeAndParentId(task);
            task = taskRepository.save(task);
            Map<String, String> fields = fetchFields(task);
            publish(fields, creator, task.getId(), task.getType(), EventType.CREATED, applicationEventPublisher);
            return toDto(TaskDto.class, task, modelMapper);
        }
        return new TaskDto();
    }

    private void fillInTask(TaskDto taskDto, Task task, User creator) {
        task.setChangedDate(LocalDateTime.now());
        setTaskCode(task);
        updateTaskVersion(task);
        fillInUsers(taskDto, task, creator);
    }

    private void fillInUsers(TaskDto taskDto, Task task, User creator) {
        if (nonNull(creator) && isNull(task.getManager())) {
            task.setManager(creator);
        }
        User assignee = getUserByEmail(taskDto.getAssigneeEmail(), userWorkflow);
        task.setAssignee(assignee);
    }

    @Override
    public TaskDto update(Long taskId, TaskDto taskDto, String userEmail, Role role) {
        Optional<Project> project = projectWorkflow.findByGroupMembers(taskDto.getProjectId(), userEmail);
        if(isAdmin(role) || project.isPresent()){
            User creator = getUserByEmail(userEmail, userWorkflow);
            Task task = taskRepository.findById(taskId).orElseThrow(supplyEntityNotFoundException(taskId, Type.TASK));
            Map<String, String> fields = getFields(taskDto, task);
            Task updatedTask = toEntity(taskDto, Task.class, modelMapper);
            updateTaskVersion(task);
            setChangedDate(updatedTask);
            modelMapper.map(updatedTask, task);
            fillTypeAndParentId(task);
            task = taskRepository.save(task);
            publish(fields, creator, task.getId(), task.getType(), EventType.EDITED, applicationEventPublisher);
            return this.toDto(TaskDto.class, task, modelMapper);
        }
        return new TaskDto();
    }

    private void fillTypeAndParentId(Task task) {
        Optional<Task> parentTask = task.getParentId() != null ? taskRepository.findById(task.getParentId()) : Optional.empty();
        if (parentTask.isEmpty() || parentTask.get().getId().equals(task.getId())
        || parentTask.get().getType() == Type.SUBTASK){
            task.setParentId(null);
            task.setType(Type.TASK);
        }else {
            task.setType(Type.SUBTASK);
        }
    }

    @Override
    public TaskDto delete(Long taskId, String userEmail, Role role) {
        Task task = isAdmin(role) ? taskRepository.findById(taskId).orElseThrow(supplyEntityNotFoundException(taskId, Type.TASK)) :
                taskRepository.findByIdAndManagerEmail(taskId, userEmail).orElseThrow(supplyEntityNotFoundException(taskId, Type.TASK));
        TaskDto taskDto = toDto(TaskDto.class, task, modelMapper);
        if (isAdmin(role)) {
            taskRepository.delete(task);
        } else {
            taskDto.setIds(Collections.singletonList(taskDto.getId()));
            archive(taskDto, userEmail, role);
        }
        return taskDto;
    }

    @Override
    public Page<TaskDto> getAllPaginated(Pageable pageable, String userEmail, Role role) {
        Page<Task> tasks = isAdmin(role) ? taskRepository.findAll(pageable) :
                taskRepository.findAllByProjectGroupsUsersEmail(pageable, userEmail);
        return tasks.map(e -> this.toDto(TaskDto.class, e, modelMapper));
    }

    @Override
    public List<String> getTasksName(String userEmail, Role role) {
        return isAdmin(role) ?
                taskRepository.findAll()
                .stream()
                .map(Task::getName)
                .collect(Collectors.toList()) :
                taskRepository.findAllByProjectGroupsUsersEmail(userEmail)
                        .stream()
                        .map(Task::getName)
                        .collect(Collectors.toList());
    }

    @Override
    public void move(TaskDto taskDto, String userEmail, Role role) {
        List<Long> ids = taskDto.getIds();
        ProjectDto project = projectWorkflow.fetchById(taskDto.getProjectId(), userEmail, role);
        if (!CollectionUtils.isEmpty(ids) && nonNull(project)) {
            List<Task> tasks = taskRepository.findAllById(ids);
            tasks.stream().filter(e -> e.getType() != Type.SUBTASK).forEach(e -> {
                e.setType(Type.SUBTASK);
                e.setParentId(taskDto.getId());
            });
        }
    }

    @Override
    public void archive(TaskDto taskDto, String userEmail, Role role) {
        List<Long> ids = taskDto.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            List<Task> tasks = isAdmin(role) ? taskRepository.findAllById(ids)
                    : taskRepository.findAllByIdInAndManagerEmail(ids, userEmail);
            tasks.forEach(e -> e.setArchived(true));
            taskRepository.saveAll(tasks);
        }
    }

    @Override
    public Optional<Task> findByProjectGroupsMember(Long taskId, String userEmail) {
        return taskRepository.findByIdAndProjectGroupsUsersEmail(taskId, userEmail);
    }

    @Override
    public Set<Task> findByProjectIdAndGroupMember(Long projectId, String userEmail) {
        return taskRepository.findByProjectIdAndProjectGroupsUsersEmail(projectId, userEmail);
    }

    private void updateTaskVersion(Task task) {
        String version = task.getVersion();
        if (StringUtils.isEmpty(version)) {
            version = String.format(DEFAULT_VERSION, 1);
        } else {
            int intVersion = Integer.parseInt(version.substring(2));
            version = String.format(DEFAULT_VERSION, ++intVersion);
        }
        task.setVersion(version);
    }

    private void setTaskCode(Task task) {
        Long projectId = task.getProject().getId();
        Task taskWithMaxId = taskRepository.findTopByOrderByIdDesc();
        Long taskId = taskWithMaxId != null ? taskWithMaxId.getId() : 0;
        String code = CodeGenerator.generateTaskCode(projectId, ++taskId);
        task.setCode(code);
    }

    private void setChangedDate(Task task) {
        task.setChangedDate(LocalDateTime.now());
    }

    private Map<String, String> getFields(TaskDto taskDto, Task task) {
        taskDto.setId(task.getId());
        Task taskForCompare = toEntity(taskDto, Task.class, modelMapper);
        return fetchEntityDifference(task, taskForCompare);
    }
}
