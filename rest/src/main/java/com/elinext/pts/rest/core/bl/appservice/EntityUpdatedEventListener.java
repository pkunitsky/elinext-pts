/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.appservice;

import com.elinext.pts.rest.core.dal.repository.ChangeLogRepository;
import com.elinext.pts.rest.model.entity.ChangeLog;
import com.elinext.pts.rest.model.reference.EntityUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Performs the logic for saving any update occurs for basic entities.
 *
 * @author Natallia Paklonskaya
 */

@Component
@AllArgsConstructor
public class EntityUpdatedEventListener {

    private static final String SEMICOLON = ";";
    private static final String PREFIX = "[";
    private static final String SUFFIX = "]";

    private final ChangeLogRepository changeLogRepository;

    @EventListener
    public void saveChangeLog(EntityUpdatedEvent entityUpdatedEvent) {
        ChangeLog changeLog = new ChangeLog();
        changeLog.setEntityType(entityUpdatedEvent.getType());
        changeLog.setEntityId(entityUpdatedEvent.getId());
        changeLog.setEventType(entityUpdatedEvent.getEventType());
        changeLog.setCreatedDate(LocalDateTime.now());
        changeLog.setDescription(parseFieldsToString(entityUpdatedEvent));
        changeLog.setUser(entityUpdatedEvent.getCreator());
        changeLogRepository.save(changeLog);
    }

    private String parseFieldsToString(EntityUpdatedEvent entityUpdatedEvent) {
        return entityUpdatedEvent.getEntityFields().entrySet().stream()
                .map(Map.Entry::toString)
                .collect(Collectors.joining(SEMICOLON, PREFIX, SUFFIX));
    }
}
