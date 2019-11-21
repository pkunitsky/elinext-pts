/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.util.MapUtil;
import com.elinext.pts.rest.model.entity.*;
import com.elinext.pts.rest.model.exception.EntityNotFoundException;
import com.elinext.pts.rest.model.reference.EntityUpdatedEvent;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.ValueChange;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Default interface for workflow business logic.
 *
 * @author Natallia Paklonskaya
 */

public interface DefaultWorkflow<T, E> {

    String EMPTY = "";
    String NULL = "null";

    default boolean isAdmin(Role role) {
        return role == Role.ADMIN;
    }

    default boolean isAdminOrManager(Role role) {
        return role == Role.ADMIN || role == Role.MANAGER;
    }

    default boolean isDifferentFields(Map<String, String> fields) {
        return !fields.isEmpty();
    }

    default Supplier<EntityNotFoundException> supplyEntityNotFoundException(Long groupId, Type type) {
        return () -> new EntityNotFoundException(groupId, type);
    }

    default User getUserByEmail(String email, UserWorkflow userWorkflow) {
        return userWorkflow.findByEmail(email);
    }

    /**
     * Converts dto to class type entity
     *
     * @return converted entity
     */
    default E toEntity(T t, Class<E> eClass, ModelMapper modelMapper) {
        return modelMapper.map(t, eClass);
    }

    /**
     * Converts entity to dto
     *
     * @return converted dto
     */
    default T toDto(Class<T> eClass, E e, ModelMapper modelMapper) {
        return modelMapper.map(e, eClass);
    }

    /**
     * Merges dto and entity
     *
     * @return merged entity
     */
    default void mergeUpdatedAndCurrentEntity(E updatedEntity, E currentEntity, ModelMapper modelMapper) {
        modelMapper.map(updatedEntity, currentEntity);
    }

    /**
     * Triggers publishing of event
     */
    default void publish(Map<String, String> fields, User creator, Long id, Type type, EventType eventType, ApplicationEventPublisher eventPublisher) {
        var entityUpdatedEvent = toEvent(fields, creator, type, id, eventType);
        eventPublisher.publishEvent(entityUpdatedEvent);
    }

    /**
     * Fetches fields from object and put them to {@link Map} filtering null
     *
     * @return map with key - name of the field, value - value of the field
     */
    default Map<String, String> fetchFields(E e) {
        var projectFields = MapUtil.parseObjectIntoMap(e.toString());
        return projectFields.entrySet()
                .stream()
                .filter(f -> !f.getValue().equalsIgnoreCase(NULL))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Compare two objects using {@link Javers}
     *
     * @return map with key -  previous value, value - new value
     */
    default Map<String, String> fetchEntityDifference(E entity, E entityForComparing) {
        var javers = JaversBuilder.javers().build();
        var diff = javers.compare(entity, entityForComparing);
        var diffChanges = diff.getChangesByType(ValueChange.class);
        return diffChanges.stream()
                .filter(e -> e.getRight() != null && e.getLeft() != null)
                .collect(Collectors.toMap(
                        PropertyChange::getPropertyName,
                        m -> Objects.toString(m.getLeft(), EMPTY) + ", " + Objects.toString(m.getRight(), EMPTY)));
    }

    private EntityUpdatedEvent toEvent(Map<String, String> entityFields, User creator, Type type, Long id, EventType eventType) {
        var entityUpdatedEvent = new EntityUpdatedEvent();
        entityUpdatedEvent.setEntityFields(entityFields);
        entityUpdatedEvent.setCreator(creator);
        entityUpdatedEvent.setType(type);
        entityUpdatedEvent.setId(id);
        entityUpdatedEvent.setEventType(eventType);
        return entityUpdatedEvent;
    }
}
