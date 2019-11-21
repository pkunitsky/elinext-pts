/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.GroupWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.GroupDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "/groups", description = "Operations with groups")
@RestController
@AllArgsConstructor
public class GroupResource {

    private GroupWorkflow groupWorkflow;

    @ApiOperation(value = "Find group by id", response = GroupDto.class)
    @GetMapping("/groups/{id}")
    public GroupDto getGroup(@PathVariable("id") Long groupId,
                             @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                             @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return groupWorkflow.fetchByIdAndGroupMember(groupId, userEmail, role);
    }

    @ApiOperation(value = "Return paginated groups", response = Page.class)
    @GetMapping(value = "/groups")
    public Page<GroupDto> getAllPaginated(Pageable pageable,
                                          @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                          @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return groupWorkflow.getAllPaginated(pageable, userEmail, role);
    }

    @ApiOperation(value = "Create new group", response = GroupDto.class)
    @PostMapping(value = "/groups")
    public GroupDto add(@Validated @RequestBody GroupDto groupDto,
                        @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                        @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return groupWorkflow.create(groupDto, userEmail, role);
    }

    @ApiOperation(value = "Update group by id", response = GroupDto.class)
    @PutMapping("/groups/{id}")
    public GroupDto update(@PathVariable("id") Long id,
                           @RequestBody GroupDto groupDto,
                           @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                           @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return groupWorkflow.update(id, groupDto, userEmail, role);
    }

    @ApiOperation("Delete group by id")
    @DeleteMapping("/groups/{id}")
    public GroupDto delete(@PathVariable("id") Long id,
                           @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                           @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return groupWorkflow.delete(id, userEmail, role);
    }
}

