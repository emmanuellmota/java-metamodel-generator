package com.emmanuellmota.metamodel;

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
