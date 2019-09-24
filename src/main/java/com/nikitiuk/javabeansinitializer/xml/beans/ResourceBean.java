package com.nikitiuk.javabeansinitializer.xml.beans;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceBean {

    private static final Logger logger =  LoggerFactory.getLogger(ResourceBean.class);

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
        logger.info("That's a resource's main method invoked;");
    }
}