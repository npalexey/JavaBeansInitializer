package com.nikitiuk.javabeansinitializer.server.request.types;

import java.lang.reflect.Method;
import java.security.Principal;

public class RequestContext {

    private Method method;
    private String securityData;
    private Principal principal;

    public RequestContext() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setSecurityData(String securityData) {
        this.securityData = securityData;
    }

    public String getSecurityData() {
        return securityData;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}