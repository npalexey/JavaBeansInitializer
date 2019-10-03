package com.nikitiuk.javabeansinitializer.server.request.types;

import com.nikitiuk.javabeansinitializer.server.request.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public interface Request {

    String getUrl();

    RequestMethod getHttpMethod();

    Map<String, String> getHeaders();

    RequestContext getRequestContext();

    default Map<String, byte[]> getBody() {
        return new HashMap<>();
    }
}
