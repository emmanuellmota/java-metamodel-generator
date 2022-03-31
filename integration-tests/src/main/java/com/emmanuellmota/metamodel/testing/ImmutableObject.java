package com.emmanuellmota.metamodel.testing;

import com.emmanuellmota.metamodel.GenerateModel;

/**
 * Example for read-only bean.
 */
@GenerateModel
public class ImmutableObject {
    private final String name;

    public ImmutableObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
