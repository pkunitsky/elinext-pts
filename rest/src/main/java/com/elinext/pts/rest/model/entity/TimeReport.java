/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This is a class for "time report" entity.
 *
 * @author Natallia Paklonskaya
 */

@Entity
@Table(name = "time_report")
@Data
@NoArgsConstructor
public class TimeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hours", nullable = false)
    private Double hours;

    @Column(name = "reported_date")
    private LocalDateTime reportedDate;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "percentage")
    private Integer percentage;

    @DiffIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference(value = "task-report")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Task task;

    @DiffIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-report")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User reporter;
}
