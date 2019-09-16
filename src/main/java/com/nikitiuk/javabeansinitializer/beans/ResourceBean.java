package com.nikitiuk.javabeansinitializer.beans;

import org.apache.commons.lang3.StringUtils;

public class ResourceBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Resource Bean Name: %s", StringUtils.defaultIfBlank(name, "no name"));
    }

    public void someInitMethod() {
        System.out.println("That's a resource's main method invoked;");
    }
}