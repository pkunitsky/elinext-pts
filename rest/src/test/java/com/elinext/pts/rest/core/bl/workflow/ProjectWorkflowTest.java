package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.ProjectRepository;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectWorkflowTest {

    @Mock
    private ProjectRepository projectRepository;

    private List<Project> projectList;
    private ProjectDto projectDto;

    @Before
    public void initProjects() {
        projectList = new ArrayList<>();
        Project firstProject = new Project();
        firstProject.setId(1L);
        firstProject.setName("Amazing Project");
        Project secondProject = new Project();
        secondProject.setId(2L);
        secondProject.setParentId(1L);
        secondProject.setName("Not Amazing Project");
        projectList.add(firstProject);
        projectList.add(secondProject);
    }

    @Before
    public void initProjectDto() {
        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Project");
    }

    @Test
    public void testAllProjectTest() {
        when(projectRepository.findAll()).thenReturn(projectList);
        List<Project> empList = projectRepository.findAll();
        assertEquals(2, empList.size());
    }

    @Test
    public void testProjectById() {
        ProjectWorkflow test = mock(ProjectWorkflow.class);
        when(test.fetchById(1L,  "lalal@Mail.ru", Role.EMPLOYEE)).thenReturn(projectDto);
        assertEquals(test.fetchById(1L, "lalal@Mail.ru", Role.EMPLOYEE), projectDto);
    }

    @Test
    public void testProjectCreation(){
        ProjectWorkflow projectWorkflow = mock(ProjectWorkflow.class);
        ProjectDto message = ProjectDto.builder().message("Forbidden").build();
        when(projectWorkflow.create(projectDto, "lalal@Mail.ru", Role.EMPLOYEE))
                .thenReturn(message);
        assertEquals(projectWorkflow.create(projectDto, "lalal@Mail.ru", Role.EMPLOYEE), message);
    }
}
