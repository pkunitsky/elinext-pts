/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.reference;


import com.elinext.pts.rest.core.bl.appservice.EntityUpdatedEventListener;
import com.elinext.pts.rest.model.entity.EventType;
import com.elinext.pts.rest.model.entity.Type;
import com.elinext.pts.rest.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Basic class used for {@link EntityUpdatedEventListener}
 *
 * @author Natallia Paklonskaya
 */

@NoArgsConstructor
@Data
public class EntityUpdatedEvent {

    private Map<String, String> entityFields;
    private User creator;
    private Type type;
    private Long id;
    private EventType eventType;
}
