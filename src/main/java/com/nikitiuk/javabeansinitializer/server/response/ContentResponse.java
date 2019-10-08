package com.nikitiuk.javabeansinitializer.server.response;

import java.io.BufferedInputStream;
import java.util.List;

public class ContentResponse implements Response {

    private List<String> headers;
    private BufferedInputStream body;

    private ContentResponse() {
    }

    public ContentResponse(List<String> headers, BufferedInputStream body) {
        this.headers = headers;
        this.body = body;
    }

    @Override
    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public BufferedInputStream getBody() {
        return body;
    }

    public void setBody(BufferedInputStream body) {
        this.body = body;
    }
}
