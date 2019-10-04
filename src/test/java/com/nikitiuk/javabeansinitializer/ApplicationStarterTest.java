package com.nikitiuk.javabeansinitializer;

import com.nikitiuk.javabeansinitializer.server.ApplicationStarter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationStarterTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStarterTest.class);
    //private static MultiThreadedServer server = new MultiThreadedServer(7070);
    /*private static final class Lock { }
    private final Object lock = new Lock();*/

    @BeforeAll
    private static void initialize() {
        ApplicationStarter.startApp(7070, ApplicationStarterTest.class.getPackage().getName());
    }

    @AfterAll
    public static void stopServer() {
        ApplicationStarter.stopApp();
    }

    @Test
    public void testControllersWithServerRequest() throws Exception {
        String url = "http://localhost:7070/docs/age";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //int responseCode = con.getResponseCode();
        logger.info("Sending 'GET' request to URL : " + url);

        /*synchronized (lock) {
            lock.wait(5000L);
        }*/
        //wait(5000L);
        //logger.info("Response Code : " + responseCode);

        /*BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info(response.toString());*/
    }
}