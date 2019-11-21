package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.UserRepository;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.model.entity.User;
import com.elinext.pts.rest.presentation.dto.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserWorkflowImpl implements UserWorkflow {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<String> getEmails() {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users
                .stream()
                .map(e -> toDto(UserDto.class, e, modelMapper))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Set<UserDto> findUsersNotInGroup(Set<User> membersOfGroup) {
        Set<User> availableUsers = userRepository.findByIdNotIn(membersOfGroup
                .stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        return availableUsers
                .stream()
                .map(e -> toDto(UserDto.class, e, modelMapper))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> findAllByGroupId(Long groupId) {
        return userRepository.findAllByGroupId(groupId);
    }

    @Override
    public Set<User> findAllByGroupId(List<Long> groupIds) {
        return userRepository.findAllByGroupIdIn(groupIds);
    }

    @Override
    public Set<String> findManagerAndAdminEmails() {
        Set<User> users = userRepository.findByRoleIn(List.of(Role.ADMIN, Role.MANAGER));
        return users
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }
}
