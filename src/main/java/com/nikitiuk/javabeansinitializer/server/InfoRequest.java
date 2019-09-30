package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoRequest implements Request {

    private final String url;
    private final RequestMethod requestMethod;

    private InfoRequest() {
        this.url = "";
        this.requestMethod = RequestMethod.GET;
    }

    public InfoRequest(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
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
    public Map<String, byte[]> getBody() {
        return new HashMap<>();
    }

    /*@Override
    public List<File> getAttachments() {
        return new ArrayList<>();
    }*/
}
