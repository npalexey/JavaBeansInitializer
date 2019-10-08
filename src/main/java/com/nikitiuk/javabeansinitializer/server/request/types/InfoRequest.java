package com.nikitiuk.javabeansinitializer.server.request.types;

import com.nikitiuk.javabeansinitializer.server.request.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class InfoRequest implements Request {

    private final String url;
    private final RequestMethod requestMethod;
    private final Map<String, String> headers;
    private RequestContext requestContext;

    private InfoRequest() {
        this.url = "";
        this.requestMethod = RequestMethod.GET;
        this.headers = new HashMap<>();
    }

    public InfoRequest(String url, RequestMethod requestMethod, Map<String, String> headers) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.headers = headers;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public RequestMethod getHttpMethod() {
        return requestMethod;
    }

    @Override
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public RequestContext getRequestContext() {
        return requestContext;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }
}
