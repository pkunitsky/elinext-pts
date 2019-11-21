/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.GroupRepository;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.presentation.dto.GroupDto;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Group workflow.
 *
 * @author Denis Senchenko
 */
@Service
@AllArgsConstructor
public class GroupWorkflowImpl implements GroupWorkflow {

    private static final String FORBIDDEN = "Forbidden";

    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserWorkflow userWorkflow;
    private final ProjectWorkflow projectWorkflow;

    @Override
    public Page<GroupDto> getAllPaginated(Pageable pageable, String userEmail, Role role) {
        var groups = isAdmin(role) ?
                groupRepository.findAll(pageable) :
                groupRepository.findAllByUsersEmail(pageable, userEmail);
        return groups.map(group -> toDto(GroupDto.class, group, modelMapper));
    }

    @Override
    public GroupDto fetchByIdAndGroupMember(Long groupId, String email, Role role) {
        var group = isAdmin(role) ?
                groupRepository.findById(groupId).orElseThrow(supplyEntityNotFoundException(groupId, Type.GROUP)) :
                groupRepository.findByIdAndUsersEmail(groupId, email).orElseThrow(supplyEntityNotFoundException(groupId, Type.GROUP));
        var availableUsers = userWorkflow.findUsersNotInGroup(group.getUsers());
        var groupDto = toDto(GroupDto.class, group, modelMapper);
        groupDto.setAvailableUsers(availableUsers);
        return groupDto;
    }

    @Override
    public GroupDto create(GroupDto groupDto, String userEmail, Role role) {
        return isAdminOrManager(role) ?
                create(groupDto, userEmail) :
                GroupDto.builder().message(FORBIDDEN).build();
    }

    private GroupDto create(GroupDto groupDto, String userEmail) {
        var creator = getUserByEmail(userEmail, userWorkflow);
        var group = this.toEntity(groupDto, Group.class, modelMapper);
        fillInProjects(group, userEmail, groupDto);
        group = groupRepository.save(group);
        var fields = fetchFields(group);
        publish(fields, creator, group.getId(), Type.GROUP, EventType.CREATED, applicationEventPublisher);
        return toDto(GroupDto.class, group, modelMapper);
    }

    @Override
    public GroupDto update(Long id, GroupDto groupDto, String userEmail, Role role) {
        var group = isAdmin(role) ?
                groupRepository.findById(id).orElseThrow(supplyEntityNotFoundException(id, Type.GROUP)) :
                groupRepository.findByIdAndUsersEmailAndUsersRole(id, userEmail, Role.MANAGER).orElseThrow(supplyEntityNotFoundException(id, Type.GROUP));
        var user = getUserByEmail(userEmail, userWorkflow);
        var updatedGroup = toEntity(groupDto, Group.class, modelMapper);
        updatedGroup.setId(group.getId());
        fillInProjects(group, userEmail, groupDto);
        group = groupRepository.save(updatedGroup);
        var fields = fetchFields(group);
        publish(fields, user, group.getId(), Type.GROUP, EventType.EDITED, applicationEventPublisher);
        return toDto(GroupDto.class, group, modelMapper);
    }

    private void fillInProjects(Group group, String userEmail, GroupDto groupDto) {
        var ids = groupDto.getProjectIds();
        if (CollectionUtils.isNotEmpty(ids)) {
            var projects = projectWorkflow.findByManagerEmailAndProjectIds(userEmail, ids);
            group.setProjects(projects);
        }
    }

    @Override
    public GroupDto delete(Long groupId, String email, Role role) {
        var group = isAdmin(role) ?
                groupRepository.findById(groupId).orElseThrow(supplyEntityNotFoundException(groupId, Type.GROUP)) :
                groupRepository.findByIdAndUsersEmailAndUsersRole(groupId, email, Role.MANAGER).orElseThrow(supplyEntityNotFoundException(groupId, Type.GROUP));
        groupRepository.delete(group);
        return toDto(GroupDto.class, group, modelMapper);
    }
}