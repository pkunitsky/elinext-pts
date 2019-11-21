/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.exception;

import com.elinext.pts.rest.model.entity.Type;

/**
 * General exception when entity is not found
 *
 * @author Natallia Paklonskaya
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id, Type type) {
        super("" + type.name() + " with id not found: " + id);
    }
}
