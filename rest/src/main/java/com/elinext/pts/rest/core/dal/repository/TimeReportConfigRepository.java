package com.elinext.pts.rest.core.dal.repository;

import com.elinext.pts.rest.model.entity.TimeReportConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeReportConfigRepository extends JpaRepository<TimeReportConfiguration, Long>, JpaSpecificationExecutor<TimeReportConfiguration> {

    TimeReportConfiguration findByName(String name);
}
