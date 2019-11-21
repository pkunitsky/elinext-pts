/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.bl.appservice.EntityUpdatedEventListener;
import com.elinext.pts.rest.core.dal.repository.ProjectRepository;
import com.elinext.pts.rest.core.util.CodeGenerator;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.model.exception.ValidationException;
import com.elinext.pts.rest.model.reference.ProjectCreatedEvent;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * The interface Project Workflow. Performs the business logic for project entity.
 *
 * @author Natallia Paklonskaya
 */

public interface ProjectWorkflow extends DefaultWorkflow<ProjectDto, Project> {

    /**
     * Creates new project entity and triggers event listener {@link EntityUpdatedEventListener}
     * for creating change log record
     *
     * @param projectDto request body
     * @param userEmail  who creates record
     * @param role       Manager/Admin is able to create a record
     */
    ProjectDto create(ProjectDto projectDto, String userEmail, Role role);

    /**
     * Fetches page interface with project dto
     *
     * @param pageable {@link Pageable} interface for pagination information
     * @return list of project dto
     */
    Page<ProjectDto> fetchProjects(Pageable pageable, String userEmail, Role role);

    /**
     * Fetches a requested project by id
     *
     * @param id project id
     * @return project dto as response body
     */
    ProjectDto fetchById(Long id, String userEmail, Role role);

    /**
     * Deletes a project record by id
     *
     * @param id project id
     */
    ProjectDto deleteById(Long id, String email, Role role);

    /**
     * Updates a record if it presents
     *
     * @param projectDto request body
     * @param id         project id
     * @return project dto as response body
     */
    ProjectDto update(Long id, ProjectDto projectDto, String userEmail, Role role);

    /**
     * Archive records
     *
     * @return project dto as response body
     */
    void archive(ProjectDto projectDto, String userEmail, Role role);

    /**
     * Gets all project name
     */
    List<String> getProjectsName(String userEmail, Role role);

    /**
     * Gets all subproject name
     */
    List<String> getSubprojectsName(String userEmail, Role role);

    /**
     * Gets all projects by user email
     */
    List<ProjectDto> fetchUserProjects(String email);

    /**
     * Joins projects/subprojects to one project
     *
     * @param projectDto request body
     */
    void joinProjects(ProjectDto projectDto, String userEmail, Role role);

    /**
     * Moves subprojects to project
     *
     * @param projectDto request body
     * @param projectId  project id
     */
    void moveSubProjects(ProjectDto projectDto, Long projectId, String userEmail);

    /**
     * Fetches projects by id and group members
     */
    Optional<Project> findByGroupMembers(Long projectId, String userEmail);

    /**
     * Fetches projects by ids and group members
     */
    List<Project> findByManagerEmailAndProjectIds(String userEmail, List<Long> ids);

    /**
     * Triggers publishing of event
     */
    default void publishProject(Project project,
                                ApplicationEventPublisher eventPublisher,
                                ProjectRepository projectRepository,
                                User creator,
                                UserWorkflow userWorkflow) {
        if (nonNull(project.getParentId())) {
            var parentProject = projectRepository.findById(project.getParentId()).orElse(new Project());
            publishProject(new ProjectCreatedEvent(getMembers(parentProject, userWorkflow, creator), project), eventPublisher);
        } else {
            publishProject(new ProjectCreatedEvent(Set.of(creator), project), eventPublisher);
        }
    }

    private Set<User> getMembers(Project parentProject, UserWorkflow userWorkflow, User creator) {
        var ids = parentProject.getGroups()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
            var members = userWorkflow.findAllByGroupId(ids);
            members.add(creator);
            return members;
        }
        return Set.of();
    }

    /**
     * Triggers publishing of event
     */
    default void publishJoinedProject(Project project,
                                      ApplicationEventPublisher eventPublisher,
                                      ProjectRepository projectRepository,
                                      User creator,
                                      UserWorkflow userWorkflow,
                                      List<Long> ids) {
        var subProjects = projectRepository.findAllById(ids);
        List<Group> groups = new ArrayList<>();
        subProjects.stream().map(Project::getGroups).forEach(groups::addAll);
        var groupIds = groups
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(groupIds)) {
            var members = userWorkflow.findAllByGroupId(groupIds);
            members.add(creator);
            publishProject(new ProjectCreatedEvent(members, project), eventPublisher);
        }
    }

    default void fillInProject(Project project,
                               ProjectDto projectDto,
                               ProjectRepository projectRepository,
                               UserWorkflow userWorkflow) {
        project.setArchived(false);
        project.setCode(CodeGenerator.getCode(project, projectRepository.findAll().size()));
        fillInUsers(project, projectDto, userWorkflow);
        setType(project, projectDto, projectRepository);
    }

    private void setType(Project project, ProjectDto projectDto, ProjectRepository projectRepository) {
        var parentId = projectDto.getParentId();
        if (nonNull(parentId)) {
            setSubProjectType(projectRepository, parentId, project);
        } else {
            project.setType(Type.PROJECT);
        }
    }

    private void setSubProjectType(ProjectRepository projectRepository, Long parentId, Project project) {
        var parentProject = projectRepository.getOne(parentId);
        if (parentProject.getType() == Type.PROJECT) {
            project.setType(Type.SUBPROJECT);
        } else {
            throw new ValidationException("Project can not be created or updated because its parent is not 'PROJECT'");
        }
    }

    default void updateProjects(Project project, ProjectRepository projectRepository, List<Project> projects) {
        project = projectRepository.save(project);
        final var id = project.getId();
        projects.forEach(e -> e.setParentId(id));
        projectRepository.saveAll(projects);
    }

    default Project getUpdatedProject(ProjectDto projectDto,
                                      Project project,
                                      ModelMapper modelMapper,
                                      UserWorkflow userWorkflow,
                                      ProjectRepository projectRepository) {
        projectDto.setId(project.getId());
        projectDto.setArchived(false);
        var updatedProject = toEntity(projectDto, Project.class, modelMapper);
        updatedProject.setGroups(project.getGroups());
        fillInUsers(updatedProject, projectDto, userWorkflow);
        setType(updatedProject, projectDto, projectRepository);
        return updatedProject;
    }

    private void fillInUsers(Project project,
                             ProjectDto projectDto,
                             UserWorkflow userWorkflow) {
        var manager = getUserByEmail(projectDto.getManagerEmail(), userWorkflow);
        project.setManager(manager);
        var projectLead = getUserByEmail(projectDto.getProjectLeadEmail(), userWorkflow);
        project.setProjectLead(projectLead);
    }

    private void publishProject(ProjectCreatedEvent project,
                                ApplicationEventPublisher eventPublisher) {
        eventPublisher.publishEvent(project);
    }
}
