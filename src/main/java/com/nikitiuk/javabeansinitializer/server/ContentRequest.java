package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentRequest implements Request {

    private final String url;
    private final RequestMethod requestMethod;
    private final Map<String, String> headers;
    private final Map<String, byte[]> body;


    private ContentRequest() {
        this.url = "";
        this.requestMethod = RequestMethod.GET;
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
    }

    public ContentRequest(String url, RequestMethod requestMethod, Map<String, String> headers, Map<String, byte[]> body) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public RequestMethod getMethod() {
        return requestMethod;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, byte[]> getBody() {
        return body;
    }

    /*@Override
    public List<File> getAttachments() {
        return null;
    }*/
}
