package com.nikitiuk.javabeansinitializer.server;

import com.nikitiuk.javabeansinitializer.server.request.MethodCaller;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.response.Response;
import com.nikitiuk.javabeansinitializer.server.response.ResponseBuilder;
import com.nikitiuk.javabeansinitializer.server.response.ResponseSender;
import com.nikitiuk.javabeansinitializer.server.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collections;

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
                ResponseSender responseSender = new ResponseSender();
                if(request.getRequestContext().getAbortResponse() != null) {
                    responseSender.sendResponse(request.getRequestContext().getAbortResponse(), outBufferedClient);
                } else if (response instanceof Response) {
                    responseSender.sendResponse((Response) response, outBufferedClient);
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