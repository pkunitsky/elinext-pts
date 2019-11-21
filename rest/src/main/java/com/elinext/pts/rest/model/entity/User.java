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
import java.util.Set;

/**
 * This is a class for "user account" entity.
 *
 * @author Natallia Paklonskaya
 * @since 2019-05-28
 */

@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "projectLead", fetch = FetchType.LAZY)
    @JsonBackReference(value = "user-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Project> projects;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    @JsonBackReference(value = "manger-project")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Project> managerProjects;

    @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-report")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TimeReport> timeReports;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "group_id")})
    @JsonBackReference(value = "group-user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Group> group;

}
