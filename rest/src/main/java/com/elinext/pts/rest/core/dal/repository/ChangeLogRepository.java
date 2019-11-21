/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.ChangeLog;
import com.elinext.pts.rest.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {

    List<ChangeLog> findByEntityIdAndEntityTypeInAndUserEmail(Long id, List<Type> type, String userEmail);

    List<ChangeLog> findByEntityIdInAndEntityTypeInAndUserEmail(List<Long> id, List<Type> type, String userEmail);
}
