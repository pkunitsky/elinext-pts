package com.elinext.pts.rest.core.bl.appservice;

import com.elinext.pts.rest.core.dal.repository.GroupRepository;
import com.elinext.pts.rest.model.entity.Group;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.User;
import com.elinext.pts.rest.model.reference.ProjectCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class ProjectCreatedEventListener {

    private final GroupRepository groupRepository;

    @EventListener
    public void createGroup(ProjectCreatedEvent projectCreatedEvent) {
        if (nonNull(projectCreatedEvent)) {
            Project project = projectCreatedEvent.getProject();
            Set<User> members = projectCreatedEvent.getMembersOfGroup();
            fillInGroup(project, members);
        }
    }

    private void fillInGroup(Project project, Set<User> members) {
        Group group = new Group();
        group.setName("Group " + project.getName());
        group.setUsers(members);
        group.setProjects(List.of(project));
        groupRepository.save(group);
    }
}
