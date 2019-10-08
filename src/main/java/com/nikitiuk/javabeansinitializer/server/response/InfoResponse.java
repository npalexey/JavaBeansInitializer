package com.nikitiuk.javabeansinitializer.server.response;

import java.io.BufferedInputStream;
import java.util.List;

public class InfoResponse implements Response {

    private List<String> headers;

    private InfoResponse() {
    }

    public InfoResponse(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
}
