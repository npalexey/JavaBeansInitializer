package com.nikitiuk.javabeansinitializer.server;

public class ApplicationStarter {

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer(Integer.parseInt(args[0]));
        new Thread(server).start();
    }
}
