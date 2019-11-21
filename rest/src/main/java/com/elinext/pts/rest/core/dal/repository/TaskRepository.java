/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.Task;
import com.elinext.pts.rest.model.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The interface Task repository.
 *
 * @author Denis Senchenko
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * Find first by id desc task.
     *
     * @return the task
     */
    Task findTopByOrderByIdDesc();

    /**
     * Find by assignee id list.
     *
     * @return the list
     */
    List<Task> findAllByProjectGroupsUsersEmail(String userEmail);

    /**
     * Find all by type page.
     *
     * @param type     the type
     * @param pageable the pageable
     * @return the page
     */
    Page<Task> findAllByType(Type type, Pageable pageable);

    /**
     * Find by code task.
     *
     * @param code the code
     * @return the task
     */
    Task findByCode(String code);

    Optional<Task> findByIdAndProjectGroupsUsersEmail(Long id, String email);

    Page<Task> findAllByProjectGroupsUsersEmail(Pageable pageable, String email);

    Optional<Task> findByIdAndManagerEmail(Long id, String managerEmail);

    List<Task> findAllByIdInAndManagerEmail(List<Long> ids, String managerEmail);

    Set<Task> findByProjectIdAndProjectGroupsUsersEmail(Long projectId, String userEmail);
}
