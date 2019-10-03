package com.nikitiuk.javabeansinitializer.annotations;

import com.nikitiuk.javabeansinitializer.annotations.testbeans.ControllerBean;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanDependent;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanIndependent;
import com.nikitiuk.javabeansinitializer.server.MultiThreadedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class ContextInitializerTest {

    private static Logger logger = LoggerFactory.getLogger(ContextInitializerTest.class);
    private static ApplicationCustomContext applicationCustomContext;
    //private static MultiThreadedServer server = new MultiThreadedServer(7070);

    @BeforeAll
    private static void initialize() {
        applicationCustomContext = new ContextInitializer().initializeContext("com.nikitiuk.javabeansinitializer.annotations");
        //new Thread(server).start();
    }

    @AfterAll
    public static void stopServer() {
        //server.stopServer();
    }

    @Test
    public void testBeans() {
        TestBeanIndependent testBeanIndependent = (TestBeanIndependent) applicationCustomContext.getBeanContainer().get(TestBeanIndependent.class);
        TestBeanDependent testBeanDependent = (TestBeanDependent) applicationCustomContext.getBeanContainer().get(TestBeanDependent.class);
        logger.info("Counted value: " + testBeanDependent.count());
        assertEquals(testBeanIndependent.getName(), testBeanDependent.getAutoWiredBean().getName());
        assertTrue(testBeanIndependent.getBooleanValue());
    }

    @Test
    public void testControllers() {
        ControllerBean controllerBean = (ControllerBean) applicationCustomContext.getControllerContainer().get(ControllerBean.class);
        TestBeanIndependent testBeanIndependent = (TestBeanIndependent) applicationCustomContext.getBeanContainer().get(TestBeanIndependent.class);
        logger.info("Age: " + controllerBean.getAgeOfServiceBean());
        logger.info("Count of serviceBean: " + controllerBean.callCountOfServiceBean());
        assertEquals(testBeanIndependent.getSomeAge(), controllerBean.getAgeOfServiceBean());
    }

    @Test
    public void testControllersWithServerRequest() throws Exception {
        String url = "http://localhost:7070/results";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //int responseCode = con.getResponseCode();
        logger.info("Sending 'GET' request to URL : " + url);
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