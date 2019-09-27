package com.nikitiuk.javabeansinitializer.server;

public class ApplicationStarter {

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer();
        new Thread(server).start();
    }
}
