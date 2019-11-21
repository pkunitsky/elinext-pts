package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.TaskRepository;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.Task;
import com.elinext.pts.rest.model.entity.Type;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import com.elinext.pts.rest.presentation.dto.TaskDto;
import org.javers.common.collections.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TaskWorkflowImplTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    UserWorkflow userWorkflow;
    @Mock
    ProjectWorkflow projectWorkflow;

    private ModelMapper modelMapper;
    private TaskWorkflow taskWorkflow;
    private List<Task> taskList;
    private TaskDto taskDto;
    private Task firstTask;
    private Task secondTask;
    private TaskDto expectedTaskDto;
    private Project project;

    @Before
    public void initTasks() {
        expectedTaskDto = new TaskDto();
        expectedTaskDto.setId(1L);
        expectedTaskDto.setName("Test task");
        expectedTaskDto.setProjectId(1L);

        modelMapper = new ModelMapper();
        taskList = new ArrayList<>();
        project = new Project();
        firstTask = new Task();
        firstTask.setName("First task");
        secondTask = new Task();
        secondTask.setName("Second task");
        firstTask.setId(1L);
        firstTask.setName("Test task");
        secondTask.setId(2L);
        secondTask.setParentId(1L);
        taskList.add(firstTask);
        taskList.add(secondTask);
        project.setTasks(new HashSet<>(taskList));
        taskWorkflow = spy(new TaskWorkflowImpl(modelMapper, taskRepository, applicationEventPublisher, userWorkflow, projectWorkflow));
    }

    @Before
    public void initProjectDto() {
        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setCode("P001-1");
    }

    @Test
    public void getAllPaginated() {
        when(taskRepository.findAll()).thenReturn(taskList);
        List<Task> empList = taskRepository.findAll();
        assertEquals(2, empList.size());
    }

    @Test
    public void fetchByTaskId() {
        Optional<Task> task = Optional.of(taskWorkflow.toEntity(expectedTaskDto, Task.class, modelMapper));
        when(taskRepository.findById(1L)).thenReturn(task);
        TaskDto actualTaskDto = taskWorkflow.fetchByTaskId(1L, "email@mail.ru", Role.ADMIN);
        Assert.assertEquals(expectedTaskDto, actualTaskDto);
    }

    @Test
    public void getTaskByCode() {
        when(taskRepository.findByCode(any())).thenReturn(firstTask);
        when(taskWorkflow.getTaskByCode(any())).thenCallRealMethod();
        taskWorkflow.getTaskByCode("P001-1");
        verify(taskRepository, times(1)).findByCode("P001-1");
    }

    @Test
    public void create() {
        doReturn(null, null).when(taskWorkflow).fetchFields(any());
        doNothing().when(taskWorkflow).publish(any(), any(), any(), any(), any(), any());
        Task toBeReturned = taskWorkflow.toEntity(expectedTaskDto, Task.class, modelMapper);
        doReturn(toBeReturned).when(taskWorkflow).toEntity(any(), any(), any());
        doReturn(toBeReturned).when(taskRepository).save(any());
        TaskDto actualTaskDto = taskWorkflow.create(expectedTaskDto, "email@mail.ru", Role.ADMIN);
        Assert.assertEquals(actualTaskDto.getCode(), "P001-1");
        Assert.assertEquals(toBeReturned.getVersion(), "1.1");
    }

    @Test
    public void update() {
        doReturn(null, null).when(taskWorkflow).fetchFields(any());
        doNothing().when(taskWorkflow).publish(any(), any(), any(), any(), any(), any());
        Task toBeReturned = taskWorkflow.toEntity(expectedTaskDto, Task.class, modelMapper);
        toBeReturned.setCode("P001-1");
        toBeReturned.setVersion("1.1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(toBeReturned));
        doReturn(toBeReturned).when(taskWorkflow).toEntity(any(), any(), any());
        doReturn(toBeReturned).when(taskRepository).save(any());
        TaskDto actualTaskDto = taskWorkflow.update(1L, expectedTaskDto, "email@mail.ru", Role.ADMIN);
        Assert.assertEquals(toBeReturned.getVersion(), "1.2");
        Assert.assertNotEquals(expectedTaskDto.getChangedDate(), actualTaskDto.getChangedDate());
    }

    @Test
    public void delete() {
        Task toBeReturned = taskWorkflow.toEntity(expectedTaskDto, Task.class, modelMapper);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(toBeReturned));
        TaskDto actualTaskDto = taskWorkflow.delete(1L, "email@mail.ru", Role.ADMIN);
        verify(taskRepository, times(1)).delete(any());
        Assert.assertEquals(expectedTaskDto, actualTaskDto);
    }

    @Test
    public void getTasksNameForAdmin() {
        doReturn(taskList).when(taskRepository).findAll();
        taskWorkflow.getTasksName("email@mail.ru", Role.ADMIN);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void getTasksNameForNotAdmin() {
        doReturn(taskList).when(taskRepository).findAllByProjectGroupsUsersEmail("email@mail.ru");
        taskWorkflow.getTasksName("email@mail.ru", Role.EMPLOYEE);
        verify(taskRepository, times(1)).findAllByProjectGroupsUsersEmail(any());
    }

    @Test
    public void move() {
        expectedTaskDto.setIds(Lists.asList(1L));
        doReturn(new ProjectDto()).when(projectWorkflow).fetchById(any(), any(), any());
        doReturn(taskList).when(taskRepository).findAllById(any());
        taskWorkflow.move(expectedTaskDto, "email@mail.ru", Role.EMPLOYEE);
        Assert.assertEquals(taskList.get(0).getType(), Type.SUBTASK);
        Assert.assertEquals(taskList.get(0).getParentId(), Long.valueOf(1L));
        Assert.assertEquals(taskList.get(1).getType(), Type.SUBTASK);
        Assert.assertEquals(taskList.get(1).getParentId(), Long.valueOf(1L));
    }

    @Test
    public void archiveForAdmin() {
        expectedTaskDto.setIds(Lists.asList(1L));
        doReturn(taskList).when(taskRepository).findAllById(any());
        doReturn(taskList).when(taskRepository).saveAll(any());
        //doReturn(taskList).when(taskRepository).findAllByIdInAndManagerEmail(any(), any());
        taskWorkflow.archive(expectedTaskDto, "email@mail.ru", Role.ADMIN);
        verify(taskRepository, times(1)).saveAll(any());
        Assert.assertEquals(taskList.get(0).getArchived(), true);
        Assert.assertEquals(taskList.get(1).getArchived(), true);
    }

    @Test
    public void archiveForNotAdmin() {
        expectedTaskDto.setIds(Lists.asList(1L));
        doReturn(taskList).when(taskRepository).findAllById(any());
        doReturn(taskList).when(taskRepository).findAllByIdInAndManagerEmail(any(), any());
        taskWorkflow.archive(expectedTaskDto, "email@mail.ru", Role.EMPLOYEE);
        verify(taskRepository, times(1)).saveAll(any());
        Assert.assertEquals(taskList.get(0).getArchived(), true);
        Assert.assertEquals(taskList.get(1).getArchived(), true);
    }
}