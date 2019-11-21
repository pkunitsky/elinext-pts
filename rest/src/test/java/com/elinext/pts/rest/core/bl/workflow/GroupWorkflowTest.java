package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.GroupRepository;
import com.elinext.pts.rest.model.entity.Group;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.User;
import com.elinext.pts.rest.presentation.dto.GroupDto;
import com.elinext.pts.rest.presentation.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupWorkflowTest {

    @InjectMocks
    private GroupWorkflowImpl groupWorkflow;

    @Mock
    private UserWorkflow userWorkflow;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ModelMapper modelMapper;

    private Group testGroup;
    private GroupDto groupDto;
    private Set<UserDto> availableUsersDto;

    @Before
    public void initGroup() {
        testGroup = new Group();
        testGroup.setName("Group");
        testGroup.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.ru");
        testGroup.setUsers(Set.of(user));
    }

    @Before
    public void initGroupDto() {
        groupDto = new GroupDto();
        groupDto.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setEmail("test@mail.ru");
        groupDto.setUsers(Set.of(userDto));
        groupDto.setAvailableUsers(Set.of(userDto));
    }

    @Before
    public void initAvailableUsersDto() {
        availableUsersDto = new HashSet<>();
        UserDto user = new UserDto();
        user.setEmail("test1@mail.ru");
        availableUsersDto.add(user);
    }

    @Test
    public void test() {
        when(userWorkflow.findUsersNotInGroup(testGroup.getUsers())).thenReturn(availableUsersDto);
        when(groupRepository.findByIdAndUsersEmail(1L, "test@mail.ru")).thenReturn(Optional.of(testGroup));
        when(groupWorkflow.toDto(GroupDto.class, testGroup, modelMapper)).thenReturn(groupDto);
        assertEquals(groupDto.getUsers(), groupWorkflow.fetchByIdAndGroupMember(1L, "test@mail.ru", Role.MANAGER).getUsers());
        assertEquals(groupDto, groupWorkflow.fetchByIdAndGroupMember(1L, "test@mail.ru", Role.MANAGER));
    }
}
