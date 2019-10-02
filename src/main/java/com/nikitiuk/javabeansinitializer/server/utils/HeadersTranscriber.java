package com.nikitiuk.javabeansinitializer.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.util.Map;

@Deprecated
public class HeadersTranscriber {

    private static final Logger logger = LoggerFactory.getLogger(HeadersTranscriber.class);
    private BufferedInputStream bufferedInputStream;

    public HeadersTranscriber(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
    }

    public Map<String, String> transcribeHeaders() {
       // bufferedInputStream.
        return null;
    }
}