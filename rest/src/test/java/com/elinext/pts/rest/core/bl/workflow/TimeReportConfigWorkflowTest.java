package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.ProjectRepository;
import com.elinext.pts.rest.core.dal.repository.UserRepository;
import com.elinext.pts.rest.model.data.FilterType;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.presentation.dto.TimeReportConfigDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TimeReportConfigWorkflowTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    private TimeReportConfigDto timeReportConfigDto;
    private List<User> users;
    private List<Project> projects;

    @Before
    public void initTimeReportConfigDto() {
        timeReportConfigDto = new TimeReportConfigDto();
        timeReportConfigDto.setSortBy(SortByType.PROJECT);
        timeReportConfigDto.setSortOrder(SortOrderType.ASC);
        LinkedHashMap<FilterType, List<String>> filters = new LinkedHashMap<>();
        filters.put(FilterType.PROJECT, List.of("project1", "project2"));
        filters.put(FilterType.REPORTER, List.of("user1", "user2"));
        timeReportConfigDto.setFilters(filters);
    }

    @Before
    public void initUsers() {
        User user1 = new User();
        user1.setEmail("user1");
        user1.setId(1L);
        User user2 = new User();
        user2.setEmail("user2");
        user2.setId(2L);
        users = List.of(user1, user2);
    }

    @Before
    public void initProjects() {
        User user1 = new User();
        user1.setEmail("user1");
        user1.setId(1L);
        User user2 = new User();
        user2.setEmail("user2");
        user2.setId(2L);
        users = List.of(user1, user2);
        Project project1 = new Project();
        project1.setName("project1");
        project1.setProjectLead(users.get(0));
        Project project2 = new Project();
        project2.setName("project2");
        project2.setProjectLead(users.get(0));
        projects = List.of(project1, project2);
    }

    @Test
    public void testFilterWithUserConfig() {
        TimeReportConfigWorkflow test = mock(TimeReportConfigWorkflow.class);
        doReturn(timeReportConfigDto).when(test).fetchFilterSettings("lalal@Mail.ru", Role.ADMIN);
        when(userRepository.findAll()).thenReturn(users);
        List<String> user = userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        assertEquals(user, timeReportConfigDto.getFilters().get(FilterType.REPORTER));
    }
}
