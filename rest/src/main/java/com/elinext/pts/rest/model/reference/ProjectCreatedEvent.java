package com.elinext.pts.rest.model.reference;

import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.Set;

@AllArgsConstructor
@Data
public class ProjectCreatedEvent {

    private final Set<User> membersOfGroup;

    private final Project project;
}
