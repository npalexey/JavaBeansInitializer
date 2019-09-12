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
        if(name != null) {
            return String.format("Resource Bean Name: %s", name);
        }
        else return "No bean name";
    }

    public void someInitMethod() {
        System.out.println("That's a resource's main method invoked;");
    }
}