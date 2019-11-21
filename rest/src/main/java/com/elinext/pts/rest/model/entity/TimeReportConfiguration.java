/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This is a class for "time report configuration" entity.
 *
 * @author Natallia Paklonskaya
 */

@Entity
@Table(name = "time_report_configuration")
@Data
@NoArgsConstructor
public class TimeReportConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sort_by")
    private SortByType sortBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "sort_order")
    private SortOrderType sortOrder;

    @Column(name = "selected_filters")
    private String selectedFilters;
}

