/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByArchivedAndType(Pageable pageable, boolean archived, Type type);

    Page<Project> findByArchivedAndTypeAndGroupsUsersEmail(Pageable pageable, boolean archived, Type type, String userEmail);

    List<Project> findByGroupsUsersEmail(String userEmail);

    List<Project> findByGroupsUsersEmailAndType(String userEmail, Type type);

    List<Project> findByType(Type type);

    Optional<Project> findByIdAndGroupsUsersEmail(Long id, String userEmail);

    Optional<Project> findByIdAndProjectLeadEmailOrManagerEmail(Long id, String userEmail, String projectLeadEmail);

    List<Project> findDistinctByIdInAndManagerEmail(List<Long> ids, String userEmail);

    List<Project> findByIdInAndType(List<Long> ids, Type type);
}
