package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Request {
    String getUrl();
    RequestMethod getMethod();
    //String getBody();
    Map<String, byte[]> getBody();
    //List<File> getAttachments();
}
