package com.nikitiuk.javabeansinitializer.annotations;

import com.nikitiuk.javabeansinitializer.annotations.testbeans.ControllerBean;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanDependent;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanIndependent;
import com.nikitiuk.javabeansinitializer.server.ApplicationStarter;
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

    private static final Logger logger = LoggerFactory.getLogger(ContextInitializerTest.class);
    private static ApplicationCustomContext applicationCustomContext;
    //private static ApplicationStarter applicationStarter = new ApplicationStarter();
    //private static MultiThreadedServer server = new MultiThreadedServer(7070);
    /*private static final class Lock { }
    private final Object lock = new Lock();*/

    @BeforeAll
    private static void initialize() {
        applicationCustomContext = new ContextInitializer().initializeContext("com.nikitiuk.javabeansinitializer.annotations");
        //applicationStarter.startApp(7070, ContextInitializerTest.class.getPackage().getName());
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
}