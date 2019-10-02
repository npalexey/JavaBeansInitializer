package com.nikitiuk.javabeansinitializer.server.request.types;

import com.nikitiuk.javabeansinitializer.server.request.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public interface Request {

    String getUrl();

    RequestMethod getMethod();

    Map<String, String> getHeaders();

    default Map<String, byte[]> getBody() {
        return new HashMap<>();
    }
}
