package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileRequest implements Request {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public RequestMethod getMethod() {
        return null;
    }

    @Override
    public Map<String, byte[]> getBody() {
        return null;
    }

    /*@Override
    public List<File> getAttachments() {
        return null;
    }*/
}
