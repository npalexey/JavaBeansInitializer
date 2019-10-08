package com.nikitiuk.javabeansinitializer.server.response;

import java.io.BufferedInputStream;
import java.util.List;

public interface Response {

    List<String> getHeaders();

    default BufferedInputStream getBody() {
        return null;
    };
}