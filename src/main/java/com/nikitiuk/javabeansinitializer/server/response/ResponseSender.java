package com.nikitiuk.javabeansinitializer.server.response;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class ResponseSender {

    public void sendResponse(Response response, BufferedOutputStream outBufferedClient) throws IOException {
        for (String header : (response).getHeaders()) {
            outBufferedClient.write(header.getBytes());
        }
        if((response).getBody() != null) {
            sendBody(response,outBufferedClient);
        }
    }

    private void sendBody(Response response, BufferedOutputStream outBufferedClient) throws IOException {
        int count;
        byte[] buffer = new byte[8192]; // or 4096, or more
        while ((count = (response).getBody().read(buffer)) > 0) {
            outBufferedClient.write(buffer, 0, count);
        }
    }
}