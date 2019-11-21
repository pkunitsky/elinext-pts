package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.TaskWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.TaskDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Task resource.
 *
 * @author Denis Senchenko
 */
@Api(value = "/task", description = "Operations with tasks")
@RestController
@AllArgsConstructor
@Slf4j
public class TaskResource {

    private final TaskWorkflow taskWorkflow;

    /**
     * Gets task.
     *
     * @param userEmail the user email
     * @param userRole  the user role
     * @return the task
     */
    @ApiOperation(value = "Find task by id.", response = TaskDto.class)
    @GetMapping("/tasks/{id}")
    public TaskDto getTask(@PathVariable("id") Long taskId,
                           @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                           @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return taskWorkflow.fetchByTaskId(taskId, userEmail, userRole);
    }

    /**
     * Gets task by code.
     *
     * @param taskCode the task code
     * @return the task by code
     */
     
    @ApiOperation(value = "Find task by task code", response = TaskDto.class)
    @GetMapping("/task/{taskCode}")
    public TaskDto getTaskByCode(@PathVariable("taskCode") String taskCode) {
        return taskWorkflow.getTaskByCode(taskCode);
    }

    /**
     * Get all paginated page.
     *
     * @param pageable  the pageable
     * @param userEmail the user email
     * @param userRole  the user role
     * @return the page
     */
    @ApiOperation(value = "Return paginated tasks", response = Page.class)
    @GetMapping("/tasks")
    public Page<TaskDto> getAllPaginated(Pageable pageable,
                                         @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                         @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return taskWorkflow.getAllPaginated(pageable, userEmail, userRole);
    }

    /**
     * Create task dto.
     *
     * @param taskDto   the task dto
     * @param userEmail the user email
     * @return the task dto
     */
    @ApiOperation(value = "Create new task", response = TaskDto.class)
    @PostMapping("/tasks")
    public TaskDto create(@Validated @RequestBody TaskDto taskDto,
                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                          @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return taskWorkflow.create(taskDto, userEmail, userRole);
    }

    /**
     * Update task dto.
     *
     * @param taskDto    the task dto
     * @param userEmail  the user email
     * @param userRole   the user role
     * @return the task dto
     */
    @ApiOperation(value = "Update task by id", response = TaskDto.class)
    @PutMapping("/tasks/{id}")
    public TaskDto update(@PathVariable("id") Long taskId,
                          @RequestBody TaskDto taskDto,
                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                          @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return taskWorkflow.update(taskId, taskDto, userEmail, userRole);
    }

    /**
     * Delete.
     *
     * @param userEmail the user email
     * @param userRole  the user role
     */
    @ApiOperation(value = "Delete task by id. The real deletion from the database occurs only for the user with the role of ADMIN, otherwise the task will be archived.")
    @DeleteMapping("/tasks/{id}")
    public TaskDto delete(@PathVariable("id") Long taskId,
                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                          @ApiParam(required = false) @RequestHeader("Role") Role userRole) {
        return taskWorkflow.delete(taskId, userEmail, userRole);
    }

    @PutMapping(value = "/tasks/move")
    public void moveTasks(@Validated @RequestBody TaskDto taskDto,
                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                          @ApiParam(required = false) @RequestHeader("Role") Role role) {
        taskWorkflow.move(taskDto, userEmail, role);
    }

    @PutMapping(value = "/tasks/archive")
    public void archiveTasks(@Validated @RequestBody TaskDto taskDto,
                             @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                             @ApiParam(required = false) @RequestHeader("Role") Role role) {
        taskWorkflow.archive(taskDto, userEmail, role);
    }
}

