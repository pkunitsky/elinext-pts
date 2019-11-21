/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.ProjectRepository;
import com.elinext.pts.rest.model.entity.EventType;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.Type;
import com.elinext.pts.rest.model.exception.ValidationException;
import com.elinext.pts.rest.presentation.dto.JoinedProjectDto;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Implementation of {@link ProjectWorkflow}
 *
 * @author Natallia Paklonskaya
 */

@Service
@Slf4j
@AllArgsConstructor
public class ProjectWorkflowImpl implements ProjectWorkflow {

    private static final String FORBIDDEN = "Forbidden";

    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserWorkflow userWorkflow;

    @Override
    public ProjectDto create(ProjectDto projectDto, String userEmail, Role role) {
        return isAdminOrManager(role)
                ? create(projectDto, userEmail) :
                ProjectDto.builder().message(FORBIDDEN).build();
    }

    private ProjectDto create(ProjectDto projectDto, String userEmail) {
        var project = toEntity(projectDto, Project.class, modelMapper);
        var creator = getUserByEmail(userEmail, userWorkflow);
        fillInProject(project, projectDto, projectRepository, userWorkflow);
        project = projectRepository.save(project);
        publishProject(project,
                applicationEventPublisher,
                projectRepository,
                creator, userWorkflow);
        Map<String, String> fields = fetchFields(project);
        publish(fields, creator, project.getId(), Type.PROJECT, EventType.CREATED, applicationEventPublisher);
        return toDto(ProjectDto.class, project, modelMapper);
    }

    @Override
    public Page<ProjectDto> fetchProjects(Pageable pageable, String userEmail, Role role) {
        var projects = isAdmin(role) ?
                projectRepository.findByArchivedAndType(pageable, false, Type.PROJECT) :
                projectRepository.findByArchivedAndTypeAndGroupsUsersEmail(pageable, false, Type.PROJECT, userEmail);
        return projects.map(e -> toDto(ProjectDto.class, e, modelMapper));
    }

    @Override
    public ProjectDto fetchById(Long id, String userEmail, Role role) {
        var project = isAdmin(role) ?
                projectRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT)) :
                projectRepository.findByIdAndGroupsUsersEmail(id, userEmail).orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT));
        return toDto(ProjectDto.class, project, modelMapper);
    }

    @Override
    public ProjectDto deleteById(Long id, String email, Role role) {
        return isAdmin(role) ? delete(id) : archive(id, email);
    }

    private ProjectDto delete(Long id) {
        var project = projectRepository.findById(id)
                .orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT));
        projectRepository.delete(project);
        return toDto(ProjectDto.class, project, modelMapper);
    }

    private ProjectDto archive(Long id, String email) {
        var project = projectRepository.findByIdAndProjectLeadEmailOrManagerEmail(id, email, email)
                .orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT));
        project.setArchived(true);
        projectRepository.save(project);
        return toDto(ProjectDto.class, project, modelMapper);
    }

    @Override
    public ProjectDto update(Long id, ProjectDto projectDto, String userEmail, Role role) {
        var creator = getUserByEmail(userEmail, userWorkflow);
        var project = isAdmin(role) ?
                projectRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT)) :
                projectRepository.findByIdAndProjectLeadEmailOrManagerEmail(id, userEmail, userEmail).orElseThrow(supplyEntityNotFoundException(id, Type.PROJECT));
        var projectForCompare = getUpdatedProject(projectDto, project, modelMapper, userWorkflow, projectRepository);
        Map<String, String> fields = fetchEntityDifference(projectForCompare, project);
        if (isDifferentFields(fields)) {
            publish(fields, creator, project.getId(), project.getType(), EventType.EDITED, applicationEventPublisher);
            mergeUpdatedAndCurrentEntity(projectForCompare, project, modelMapper);
            project = projectRepository.save(project);
        }
        return toDto(ProjectDto.class, project, modelMapper);
    }

    @Override
    public void archive(ProjectDto projectDto, String userEmail, Role role) {
        var ids = projectDto.getIds();
        if (CollectionUtils.isNotEmpty(ids)) {
            var projects = isAdmin(role) ? projectRepository.findAllById(ids)
                    : projectRepository.findDistinctByIdInAndManagerEmail(ids, userEmail);
            projects.forEach(e -> e.setArchived(true));
            projectRepository.saveAll(projects);
        }
    }

    @Override
    public List<String> getProjectsName(String userEmail, Role role) {
        return isAdmin(role) ? projectRepository.findAll()
                .stream()
                .map(Project::getName)
                .collect(Collectors.toList()) : projectRepository.findByGroupsUsersEmail(userEmail)
                .stream()
                .map(Project::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSubprojectsName(String userEmail, Role role) {
        return isAdmin(role) ? projectRepository.findByType(Type.SUBPROJECT)
                .stream()
                .map(Project::getName)
                .collect(Collectors.toList()) :
                projectRepository.findByGroupsUsersEmailAndType(userEmail, Type.SUBPROJECT)
                        .stream()
                        .map(Project::getName)
                        .collect(Collectors.toList());
    }

    public List<ProjectDto> fetchUserProjects(String email) {
        var user = userWorkflow.findByEmail(email);
        if (user == null) {
            return List.of();
        }
        return user.getProjects().stream().map(project -> toDto(ProjectDto.class, project, modelMapper)).collect(Collectors.toList());
    }

    @Override
    public void joinProjects(ProjectDto projectDto, String userEmail, Role role) {
        var project = toEntity(projectDto, Project.class, modelMapper);
        var creator = getUserByEmail(userEmail, userWorkflow);
        fillInProject(project, projectDto, projectRepository, userWorkflow);
        var joinProjects = projectDto.getJoinedProjects();
        if (CollectionUtils.isNotEmpty(joinProjects)) {
            var ids = joinProjects.stream().map(JoinedProjectDto::getId).flatMap(List::stream).collect(Collectors.toList());
            var projects = projectRepository.findAllById(ids);
            updateProjects(project, projectRepository, projects);
            publishJoinedProject(project,
                    applicationEventPublisher,
                    projectRepository,
                    creator, userWorkflow, ids);
            projectRepository.flush();
            joinProjects.stream().filter(e -> e.getProjectId() != null).forEach(e -> projectRepository.deleteById(e.getProjectId()));
        }
    }

    @Override
    public void moveSubProjects(ProjectDto projectDto, Long projectId, String userEmail) {
        var project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            if (project.get().getType() == Type.PROJECT && CollectionUtils.isNotEmpty(projectDto.getIds())) {
                var projects = projectRepository.findByIdInAndType(projectDto.getIds(), Type.SUBPROJECT);
                projects.forEach(e -> e.setParentId(projectId));
                projectRepository.saveAll(projects);
            } else {
                throw new ValidationException("It's impossible to move to project, because it's 'SUBPROJECT'");
            }
        }
    }

    @Override
    public Optional<Project> findByGroupMembers(Long projectId, String userEmail) {
        return projectRepository.findByIdAndGroupsUsersEmail(projectId, userEmail);
    }

    @Override
    public List<Project> findByManagerEmailAndProjectIds(String userEmail, List<Long> ids) {
        return projectRepository.findDistinctByIdInAndManagerEmail(ids, userEmail);
    }
}
