/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.Task;
import com.elinext.pts.rest.presentation.dto.TaskDto;
import com.elinext.pts.rest.presentation.dto.TimeReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * The interface Task workflow. Business logic for tasks.
 *
 * @author Denis Senchenko
 */
public interface TaskWorkflow extends DefaultWorkflow<TaskDto, Task> {


    /**
     * Gets task by code.
     *
     */
     TaskDto fetchByTaskId(Long taskId, String userEmail, Role role);

    /**
     * Gets task by code.
     *
     * @param code the code
     * @return the task by code
     */
    TaskDto getTaskByCode(String code);

    /**
     * Create task dto.
     *
     * @param taskDto the task dto
     * @return the task dto
     */
    TaskDto create(TaskDto taskDto, String userEmail, Role role);

    /**
     * Update task dto.
     *
     * @param taskDto    the task dto
     * @return the task dto
     */
    TaskDto update(Long takId, TaskDto taskDto, String userEmail, Role role);

    /**
     * Delete.
     */
    TaskDto delete(Long taskId, String userEmail, Role role);

    /**
     * Gets all paginated.
     *
     * @param pageable the pageable
     * @return the all paginated
     */
    Page<TaskDto> getAllPaginated(Pageable pageable, String userEmail, Role role);

    /**
     * Gets tasks name.
     *
     * @return the tasks name
     */
    List<String> getTasksName(String userEmail, Role role);

    void move(TaskDto taskDto, String userEmail, Role role);

    void archive(TaskDto taskDto, String userEmail, Role role);

    /**
     * Gets a task by a group member and task id
     *
     * @return {@link TimeReportDto}
     */
    Optional<Task> findByProjectGroupsMember(Long taskId, String userEmail);


    /**
     * Gets a tasks by a group member and project id
     *
     * @return set of {@link Task}
     */
    Set<Task> findByProjectIdAndGroupMember(Long projectId, String userEmail);
}
