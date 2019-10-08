package com.nikitiuk.javabeansinitializer.server;

import com.nikitiuk.javabeansinitializer.server.request.MethodCaller;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.response.Response;
import com.nikitiuk.javabeansinitializer.server.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);
    private Socket client;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            logger.info(String.format("The Client %s:%d is connected", client.getInetAddress(), client.getPort()));
            BufferedOutputStream outBufferedClient = new BufferedOutputStream(client.getOutputStream());
            InputStream inputStream = client.getInputStream();
            try {
                Request request = HttpUtils.readRequest(inputStream);
                MethodCaller methodCaller = new MethodCaller();
                Object response = methodCaller.callRequestedMethod(request);
                if (response instanceof Response) {
                    for (String header : ((Response) response).getHeaders()) {
                        outBufferedClient.write(header.getBytes());
                    }
                    int count;
                    byte[] buffer = new byte[8192]; // or 4096, or more
                    while ((count = ((Response) response).getBody().read(buffer)) > 0) {
                        outBufferedClient.write(buffer, 0, count);
                    }
                }
            } catch (Exception e) {
                logger.error("Error while receiving request.", e);
            }
            outBufferedClient.close();
            inputStream.close();
        } catch (Exception e) {
            logger.error("Exception thrown: ", e);
        }
    }
}