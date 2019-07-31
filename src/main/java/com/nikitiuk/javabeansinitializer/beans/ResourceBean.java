package com.nikitiuk.javabeansinitializer.beans;

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
        return "Resource Bean Name: " + name;
    }

    public void someInitMethod(){
        System.out.println("That's a resource's main method invoked;");
    }
}
