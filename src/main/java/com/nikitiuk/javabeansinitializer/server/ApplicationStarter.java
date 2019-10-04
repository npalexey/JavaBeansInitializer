package com.nikitiuk.javabeansinitializer.server;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.ContextInitializer;

public class ApplicationStarter {

    private static MultiThreadedServer server;

    public static void startApp(int port, String packageName/*Class<?> appStarterClass*/) {
        ApplicationCustomContext applicationCustomContext = new ContextInitializer().initializeContext(
                packageName
                /*appStarterClass.getPackage().getName()*/
                /*"com.nikitiuk.javabeansinitializer.annotations"*/);
        server = new MultiThreadedServer(port/*Integer.parseInt(args[0])*/);
        new Thread(server).start();
    }

    public static void stopApp() {
        server.stopServer();
    }

    public static void main(String[] args) {
        startApp(7070, "com.nikitiuk.javabeansinitializer.annotations");
    }
}
