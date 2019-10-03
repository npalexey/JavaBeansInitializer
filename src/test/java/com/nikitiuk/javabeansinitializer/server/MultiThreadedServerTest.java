package com.nikitiuk.javabeansinitializer.server;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MultiThreadedServerTest {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadedServerTest.class);
    private static MultiThreadedServer server = new MultiThreadedServer(7070);

    @BeforeAll
    public static void startServer() {
        new Thread(server).start();
    }

    @AfterAll
    public static void stopServer() {
        server.stopServer();
    }

    @AfterEach
    public void getRequestTest() throws IOException {
        String url = "http://localhost:7070/results";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        /*int responseCode = con.getResponseCode();
        logger.info("Sending 'GET' request to URL : " + url);
        logger.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info(response.toString());*/
    }

    @Test
    @Order(1)
    public void postRequestTest() throws IOException {
        String url = "http://localhost:7070/mapdata";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "number=4892&name=something";

        /*con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        logger.info(String.format("Sending POST request to URL : %s", url));
        logger.info("Post parameters : " + urlParameters);
        logger.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info(response.toString());*/
    }

    @Test
    @Order(2)
    public void putRequestTest() throws IOException {
        String url = "http://localhost:7070/mapdata";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("PUT");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "number=42566&name=nothing";

        /*con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        logger.info(String.format("Sending PUT request to URL : %s", url));
        logger.info("Post parameters : " + urlParameters);
        logger.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info(response.toString());*/
    }

    @Test
    @Order(3)
    public void deleteRequestTest() throws IOException {
        String baseUrl = "http://localhost:7070/";
        String requestValueToDelete = "name";
        String url = baseUrl.concat(requestValueToDelete);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");

        /*int responseCode = con.getResponseCode();
        logger.info("Sending 'DELETE' request to URL : " + url);
        logger.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
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