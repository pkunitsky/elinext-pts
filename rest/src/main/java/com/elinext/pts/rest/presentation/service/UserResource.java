package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.UserWorkflow;
import com.elinext.pts.rest.presentation.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Api(value = "/user", description = "Operations with users")
@RestController
@Slf4j
@AllArgsConstructor
public class UserResource {

    private final UserWorkflow userWorkflow;

    @ApiOperation(value = "Returns users' emails", response = UserDto.class, responseContainer = "List")
    @GetMapping(path = "/users")
    public List<UserDto> fetchUsers() {
        return userWorkflow.getUsers();
    }

    @ApiOperation(value = "Returns users' emails whose roles are ADMIN and MANAGER", response = UserDto.class, responseContainer = "List")
    @GetMapping(path = "/users/manager-emails")
    public Set<String> fetchAdminAndManagerEmails() {
        return userWorkflow.findManagerAndAdminEmails();
    }
}
