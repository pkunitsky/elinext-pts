package com.elinext.pts.rest.presentation.service;

import com.elinext.pts.rest.core.bl.workflow.ChangeLogWorkflow;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.ChangeLogDto;
import com.elinext.pts.rest.presentation.dto.ProjectDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/logs", description = "Operations with change logs")
@RestController
@AllArgsConstructor
public class ChangeLogResource {

    private final ChangeLogWorkflow changeLogWorkflow;

    @ApiOperation(value = "Returns all change logs from root project", response = ChangeLogDto.class, responseContainer = "List")
    @GetMapping("/logs/{projectId}")
    public Page<ChangeLogDto> getChangeLogs(Pageable pageable,
                                            @PathVariable("projectId") Long projectId,
                                            @ApiParam(required = false) @RequestHeader("Email") String userEmail,
                                            @ApiParam(required = false) @RequestHeader("Role") Role role) {
        return changeLogWorkflow.fetchAllByProjectId(pageable, projectId, userEmail, role);
    }
}
