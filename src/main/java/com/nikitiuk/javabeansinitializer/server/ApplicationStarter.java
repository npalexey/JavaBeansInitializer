package com.nikitiuk.javabeansinitializer.server;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.ContextInitializer;

public class ApplicationStarter {

    public static void main(String[] args) {
        ApplicationCustomContext applicationCustomContext = new ContextInitializer().initializeContext("com.nikitiuk.javabeansinitializer.annotations");
        MultiThreadedServer server = new MultiThreadedServer(Integer.parseInt(args[0]));
        new Thread(server).start();
    }
}
