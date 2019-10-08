package com.nikitiuk.javabeansinitializer.server.request.types;

import com.nikitiuk.javabeansinitializer.server.response.Response;

import java.lang.reflect.Method;

public class RequestContext {

    private Method method;
    private String securityInfo;
    private ISecurityContext iSecurityContext;
    private Response abortResponse;

    public RequestContext() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getSecurityInfo() {
        return securityInfo;
    }

    public void setSecurityInfo(String securityInfo) {
        this.securityInfo = securityInfo;
    }

    public ISecurityContext getISecurityContext() {
        return iSecurityContext;
    }

    public void setISecurityContext(ISecurityContext iSecurityContext) {
        this.iSecurityContext = iSecurityContext;
    }

    public Response getAbortResponse() {
        return abortResponse;
    }

    public void abortWith(Response response) {
        this.abortResponse = response;
    }
}