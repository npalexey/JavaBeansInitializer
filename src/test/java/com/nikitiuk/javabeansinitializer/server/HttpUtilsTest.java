package com.nikitiuk.javabeansinitializer.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {

    @Test
    void readRequest() throws IOException {

        HttpUtils.readRequest( HttpUtilsTest.class.getResourceAsStream("/sample.xml"));
    }
}