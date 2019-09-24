package com.nikitiuk.javabeansinitializer.annotations;

import java.util.Map;

public class ApplicationCustomContext {

    private Map<Class, Object> beanContainer;
    private Map<Class, Object> controllerContainer;

    public Map<Class, Object> getBeanContainer() {
        return beanContainer;
    }

    public void setBeanContainer(Map<Class, Object> beanContainer) {
        this.beanContainer = beanContainer;
    }

    public Map<Class, Object> getControllerContainer() {
        return controllerContainer;
    }

    public void setControllerContainer(Map<Class, Object> controllerContainer) {
        this.controllerContainer = controllerContainer;
    }
}
