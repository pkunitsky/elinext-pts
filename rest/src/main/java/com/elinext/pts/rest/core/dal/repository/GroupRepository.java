/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.Group;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.GroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Group repository.
 *
 * @author Denis Senchenko
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByIdAndUsersEmail(Long id, String userEmail);

    Optional<Group> findByIdAndUsersEmailAndUsersRole(Long id, String userEmail, Role role);

    Page<Group> findAllByUsersEmail(Pageable pageable, String userEmail);
}
