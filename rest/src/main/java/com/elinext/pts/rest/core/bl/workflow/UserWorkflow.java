package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.User;
import com.elinext.pts.rest.presentation.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserWorkflow extends DefaultWorkflow<UserDto, User> {

    /**
     * Fetches the specified user by email
     */
    User findByEmail(String email);

    /**
     * Fetches all users
     */
    List<UserDto> getUsers();

    /**
     * Fetches all users' emails
     */
    List<String> getEmails();


    /**
     * Fetches all users not in the group
     */
    Set<UserDto> findUsersNotInGroup(Set<User> membersOfGroup);

    /**
     * Fetches all users which members of specified groups
     * @param groupId specifies group
     */
    Set<User> findAllByGroupId(Long groupId);

    /**
     * Fetches all users which members of specified groups
     */
    Set<User> findAllByGroupId(List<Long> groupIds);

    /**
     * Fetches all users' emails if they are mangers or admins
     */
    Set<String> findManagerAndAdminEmails();
}
