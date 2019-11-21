/*
 * Copyright (c) 2019. Elinext.
 */
package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.model.entity.ChangeLog;
import com.elinext.pts.rest.model.entity.Role;
import com.elinext.pts.rest.presentation.dto.ChangeLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Change Log Workflow. Performs the business logic for change log entity.
 *
 * @author Natallia Paklonskaya
 */

public interface ChangeLogWorkflow extends DefaultWorkflow<ChangeLogDto, ChangeLog> {

    Page<ChangeLogDto> fetchAllByProjectId(Pageable pageable, Long projectId, String userEmail, Role role);
}
