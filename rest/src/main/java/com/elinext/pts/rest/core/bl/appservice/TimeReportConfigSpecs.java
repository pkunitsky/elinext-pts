/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.appservice;

import com.elinext.pts.rest.core.util.FilterUtil;
import com.elinext.pts.rest.core.util.StatusUtil;
import com.elinext.pts.rest.model.entity.TimeReport;
import com.elinext.pts.rest.model.entity.Status;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Type;
import com.elinext.pts.rest.model.reference.FilterCriteria;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.time.LocalDateTime;


import static java.util.Objects.nonNull;

/**
 * Performs realization for complex filtering
 *
 * @author Natallia Paklonskaya
 */

public final class TimeReportConfigSpecs {

    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String REPORTER = "reporter";
    private static final String EMAIL = "email";
    private static final String PROJECT = "project";
    private static final String REPORTED_DATE = "reportedDate";
    private static final String TYPE = "type";

    private TimeReportConfigSpecs() {
    }

    public static Specification<TimeReport> findByCriteria(List<FilterCriteria> filterCriteria) {
        return (root, query, cb) -> {
            Predicate predicate = cb.disjunction();
            filterCriteria.forEach(criteria -> {
                if (nonNull(criteria.getFilterType())) {
                    switch (criteria.getFilterType()) {
                        case REPORTER:
                            filterUser(root, predicate, cb, criteria);
                            break;
                        case PROJECT_STATUS:
                            filterProjectStatuses(root, predicate, cb, criteria);
                            break;
                        case PROJECT:
                            filterProject(root, predicate, cb, criteria);
                            break;
                        case TASK:
                            filterTask(root, predicate, cb, criteria);
                            break;
                        case PERIOD:
                            filterPeriod(root, predicate, cb, criteria);
                            break;
                        case SUBPROJECT:
                            filterSubproject(root, predicate, cb, criteria);
                            break;
                        case TASK_STATUS:
                            filterTaskStatus(root, predicate, cb, criteria);
                            break;
                    }
                }
            });
            return predicate;
        };
    }

    private static void filterTaskStatus(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        List<Status> statuses = StatusUtil.getStatuses(criteria);
        statuses.forEach(e -> predicate.getExpressions().add(criteriaBuilder.equal(root.join(TASK).get(STATUS), e)));
    }

    private static void filterTask(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        criteria.getFilterValues().forEach(e -> predicate.getExpressions().add(criteriaBuilder.equal(root.join(TASK).get(NAME), e)));
    }

    private static void filterSubproject(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        Join<Project, TimeReport> join = root.join(TASK).join(PROJECT);
        criteria.getFilterValues()
                .forEach(e -> predicate.getExpressions()
                        .add(criteriaBuilder.and(criteriaBuilder.equal(join.get(NAME), e),
                                criteriaBuilder.equal(join.get(TYPE), Type.SUBPROJECT))));
    }

    private static void filterPeriod(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        List<LocalDateTime> dates = FilterUtil.getDate(criteria);
        if (dates.size() == 2) {
            predicate.getExpressions().add(criteriaBuilder.between(root.get(REPORTED_DATE), dates.get(0), dates.get(1)));
        }
    }

    private static void filterUser(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        criteria.getFilterValues().forEach(e -> predicate.getExpressions().add(criteriaBuilder.equal(root.join(REPORTER).get(EMAIL), e)));
    }

    private static void filterProject(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        Join<Project, TimeReport> join = root.join(TASK).join(PROJECT);
        criteria.getFilterValues().forEach(e -> predicate.getExpressions()
                .add(criteriaBuilder.and(criteriaBuilder.equal(join.get(NAME), e),
                        criteriaBuilder.equal(join.get(TYPE), Type.PROJECT))));
    }

    private static void filterProjectStatuses(Root root, Predicate predicate, CriteriaBuilder criteriaBuilder, FilterCriteria criteria) {
        List<Status> statuses = StatusUtil.getStatuses(criteria);
        statuses.forEach(e -> predicate.getExpressions().add(criteriaBuilder.equal(root.join(TASK).join(PROJECT).get(STATUS), e)));
    }
}


