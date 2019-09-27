package com.nikitiuk.javabeansinitializer.server;

import java.io.File;
import java.util.List;

public interface Request {
    String getUrl();
    RequestMethod getMethod();
    String getBody();
    List<File> getAttachments();
}
