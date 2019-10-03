package com.nikitiuk.javabeansinitializer.annotations;

import java.util.Map;

public class ApplicationCustomContext {

    private Map<Class, Object> listenerContainer;
    private Map<Class, Object> securityContainer;
    private Map<Class, Object> beanContainer;
    private Map<Class, Object> controllerContainer;
    private static ApplicationCustomContext applicationCustomContext;

    private ApplicationCustomContext() {

    }

    public static ApplicationCustomContext getApplicationCustomContext() {
        if (applicationCustomContext == null) {
            applicationCustomContext = new ApplicationCustomContext();
        }
        return applicationCustomContext;
    }

    public Map<Class, Object> getListenerContainer() {
        return listenerContainer;
    }

    public void setListenerContainer(Map<Class, Object> listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    public Map<Class, Object> getSecurityContainer() {
        return securityContainer;
    }

    public void setSecurityContainer(Map<Class, Object> securityContainer) {
        this.securityContainer = securityContainer;
    }

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