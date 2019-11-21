/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * This is a class for "group" entity.
 *
 * @author Natallia Paklonskaya
 */

@Entity
@Table(name = "\"group\"")
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @DiffIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference(value = "group-user")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_account_group",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    @DiffIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference(value = "group-project")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_groups",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")})
    private List<Project> projects;
}
