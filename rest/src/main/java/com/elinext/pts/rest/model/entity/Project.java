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
import java.util.List;
import java.util.Set;


/**
 * This is a class for "project" entity.
 *
 * @author Natallia Paklonskaya
 */

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.SUBMITTED;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "changed_date")
    private LocalDateTime changedDate = LocalDateTime.now();

    @Column(name = "customer")
    private String customer;

    @Column(name = "parent_id")
    private Long parentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type = Type.PROJECT;

    @Column(name = "archived")
    private Boolean archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinColumn(name = "project_lead_id")
    @JsonManagedReference(value = "user-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User projectLead;

    @ManyToOne(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinColumn(name = "manager_id")
    @JsonManagedReference(value = "manager-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User manager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @DiffIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference(value = "task-project")
    private Set<Task> tasks;

    @ManyToMany(fetch = FetchType.LAZY)
    @DiffIgnore
    @JoinTable(
            joinColumns = {
                    @JoinColumn(name = "project_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "group_id")})
    @JsonBackReference(value =  "group-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Group> groups;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @DiffIgnore
    private List<Project> subprojects;
}

