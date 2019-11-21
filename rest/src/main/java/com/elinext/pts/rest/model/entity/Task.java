/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * This is a class for "task" entity.
 *
 * @author Natallia Paklonskaya
 */

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "changed_date")
    private LocalDateTime changedDate;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "estimated_time")
    private Double estimatedTime;

    @Column(name = "percentage")
    private Integer percentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference(value = "task-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @DiffIgnore
    @JsonManagedReference(value = "task-report")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TimeReport> timeReports;

    @ManyToOne(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinColumn(name = "assignee_id")
    @JsonBackReference(value = "reporter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinColumn(name = "manager_id")
    @JsonBackReference(value = "manager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User manager;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    @DiffIgnore
    @EqualsAndHashCode.Exclude
    private Set<Task> subtasks;
}
