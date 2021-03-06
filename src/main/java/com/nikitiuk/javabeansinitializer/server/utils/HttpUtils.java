package com.nikitiuk.javabeansinitializer.server.utils;

import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.request.RequestDigester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

final public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    public static Request readRequest(InputStream inputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        return new RequestDigester().parse(bis);
    }
}
