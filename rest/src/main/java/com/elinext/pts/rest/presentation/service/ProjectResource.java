/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.ProjectWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * CRUD project controller
 *
 * @author Natallia Paklonskaya
 */

@Api(value = "/projects", description = "Operations with projects")
@RestController
@AllArgsConstructor
public class ProjectResource {

    private final ProjectWorkflow projectWorkflow;

    @ApiOperation(value = "Create new project", response = ProjectDto.class)
    @PostMapping(value = "/projects")
    public ProjectDto add(@Validated @RequestBody ProjectDto projectDto,
                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                          @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return projectWorkflow.create(projectDto, userEmail, role);
    }

    @ApiOperation(value = "Return paginated projects", response = ProjectDto.class, responseContainer = "List")
    @GetMapping(value = "/projects")
    public Page<ProjectDto> getProjects(Pageable pageable,
                                        @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                        @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return projectWorkflow.fetchProjects(pageable, userEmail, role);
    }

    @ApiOperation(value = "Find project by id", response = ProjectDto.class)
    @GetMapping(value = "/projects/{id}")
    public ProjectDto getProject(@PathVariable("id") Long id,
                                 @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                 @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return projectWorkflow.fetchById(id, userEmail, role);
    }

    @ApiOperation(value = "Delete project by id. The real deletion from the database occurs only for the user with the role of ADMIN, otherwise the project will be archived.")
    @DeleteMapping(value = "/projects/{id}")
    public ProjectDto deleteProject(@PathVariable("id") Long id,
                                    @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                    @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return projectWorkflow.deleteById(id, userEmail, role);
    }

    @ApiOperation(value = "Update project by id", response = ProjectDto.class)
    @PutMapping(value = "/projects/{id}")
    public ProjectDto updateProject(@PathVariable("id") Long id,
                                    @Validated @RequestBody ProjectDto projectDto,
                                    @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                    @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return projectWorkflow.update(id, projectDto, userEmail, role);
    }

    @ApiOperation(value = "Archive project by id", response = ProjectDto.class)
    @PutMapping(value = "/projects/archive")
    public void archiveProjects(@RequestBody ProjectDto projectDto,
                                @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                @ApiParam(required = false) @RequestHeader("Role") Role role) {
        projectWorkflow.archive(projectDto, userEmail, role);
    }


    @PutMapping(value = "/projects/{id}/move")
    public void moveSubprojects(@Validated @RequestBody ProjectDto projectDto,
                                @PathVariable("id") Long id,
                                @ApiParam(required = false) @RequestHeader("Email") String userEmail) {
        projectWorkflow.moveSubProjects(projectDto, id, userEmail);
    }

    @PutMapping(value = "/projects/join")
    public void joinSubprojects(@Validated @RequestBody ProjectDto projectDto,
                                @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                @ApiParam(required = false) @RequestHeader("Role") Role role) {
        projectWorkflow.joinProjects(projectDto, userEmail, role);
    }

    @ApiOperation(value = "Returns projects for the user with the specified mail.", response = ProjectDto.class, responseContainer = "List")
    @GetMapping(path = "/projects/user")
    public List<ProjectDto> fetchUserProjects(@RequestParam("email") String email) {
        return projectWorkflow.fetchUserProjects(email);
    }
}
