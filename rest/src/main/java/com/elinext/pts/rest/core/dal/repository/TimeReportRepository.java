/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.TimeReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TimeReportRepository extends JpaRepository<TimeReport, Long>, JpaSpecificationExecutor<TimeReport> {

    Page<TimeReport> findByTaskId(Long id, Pageable pageable);

    Page<TimeReport> findByTaskIdAndReporterGroupUsersEmail(Pageable pageable, Long taskId, String userEmail);

    List<TimeReport> findTop5ByReporter_EmailOrderByReportedDateDesc(String reporterEmail);

    Page<TimeReport> findByReporterGroupUsersEmail(Pageable pageable, String userEmail);

    Optional<TimeReport> findByIdAndReporterEmailOrTaskManagerEmail(Long id, String reporterEmail, String managerEmail);

    Set<TimeReport> findByTaskIdInAndTaskProjectGroupsUsersEmail(List<Long> ids, String  userEmail);
}
