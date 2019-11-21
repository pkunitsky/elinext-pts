/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Set<User> findAllByGroupId(Long groupId);

    Set<User> findAllByGroupIdIn(List<Long> groupId);

    Set<User> findByIdNotIn(List<Long> ids);

    Set<User> findByRoleIn(List<Role> roles);
}
