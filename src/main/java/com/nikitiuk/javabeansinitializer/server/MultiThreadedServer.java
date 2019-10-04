package com.nikitiuk.javabeansinitializer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadedServer.class);
    private final int socketPort;
    private boolean SERVER_STOPPED = true;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private MultiThreadedServer() {
        this.socketPort = 7070;
    }

    public MultiThreadedServer(int socketPort) {
        this.socketPort = socketPort;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = openSocket(socketPort)) {
            SERVER_STOPPED = false;

            logger.info(String.format("HTTPServer started on port %d", socketPort));

            serverLoop(serverSocket);

            stopServer();
            logger.info("Server Stopped.");
        } catch (IOException e) {
            logger.error("Error while opening socket.", e);
            throw new RuntimeException("Error while opening socket.", e);
        }
    }

    private ServerSocket openSocket(int port) throws IOException {
        return new ServerSocket(port, 10, InetAddress.getByName("127.0.0.1"));
    }

    private void serverLoop(ServerSocket serverSocket) {
        while (!SERVER_STOPPED) {
            try {
                Socket connectedClientSocket = serverSocket.accept();
                /*ServerThread httpServerThread = new ServerThread(connectedClientSocket);
                httpServerThread.start();*/
                this.threadPool.execute(new ServerThread(connectedClientSocket));
            } catch (IOException e) {
                if(SERVER_STOPPED) {
                    logger.info("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection.", e);
            }
        }
    }

    public void stopServer() {
        logger.info("Stopping server.");
        SERVER_STOPPED = true;
        threadPool.shutdown();
    }
}
