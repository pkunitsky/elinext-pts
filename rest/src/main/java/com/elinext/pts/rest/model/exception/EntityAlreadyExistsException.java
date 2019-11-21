/*
 * Copyright (c) 2019. Elinext.
 */

package com.elinext.pts.rest.model.exception;

import com.elinext.pts.rest.model.entity.Type;

/**
 * General exception when entity already exists
 *
 * @author Natallia Paklonskaya
 */

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String name, Type type) {
        super("" + type.name() + " with name: " + name + " already exists");
    }
}

