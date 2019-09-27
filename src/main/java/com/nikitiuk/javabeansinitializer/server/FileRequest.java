package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.List;

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
    public String getBody() {
        return null;
    }

    @Override
    public List<File> getAttachments() {
        return null;
    }
}
