/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.Group;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.GroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Group workflow. Business logic for groups.
 *
 * @author Denis Senchenko
 */
public interface GroupWorkflow extends DefaultWorkflow<GroupDto, Group> {

    /**
     * Gets all paginated.
     *
     * @param pageable the pageable
     * @return the all paginated
     */
    Page<GroupDto> getAllPaginated(Pageable pageable, String email, Role role);

    /**
     * Gets by id and userEmail
     */
    GroupDto fetchByIdAndGroupMember(Long groupId, String email, Role role);

    /**
     * Create group dto.
     *
     * @param groupDto the group dto
     * @return the group dto
     */
    GroupDto create(GroupDto groupDto, String userEmail, Role role);

    /**
     * Update group dto.
     *
     * @param groupDto    the group dto
     * @return the group dto
     */
    GroupDto update(Long id, GroupDto groupDto, String userEmail, Role role);

    /**
     * Deletes the group
     */
    GroupDto delete(Long groupId, String email, Role role);
}
