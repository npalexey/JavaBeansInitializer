package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRequest implements Request {

    private final String url;
    private final RequestMethod requestMethod;
    private final Map<String, byte[]> body;

    private FileRequest() {
        this.url = "";
        this.requestMethod = RequestMethod.GET;
        this.body = new HashMap<>();
    }

    public FileRequest(String url, RequestMethod requestMethod, Map<String, byte[]> body) {
        this.url = url;
        this.requestMethod = requestMethod;
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
    public Map<String, byte[]> getBody() {
        return body;
    }

    /*@Override
    public List<File> getAttachments() {
        return null;
    }*/
}
