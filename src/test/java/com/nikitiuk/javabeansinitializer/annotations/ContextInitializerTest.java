package com.nikitiuk.javabeansinitializer.annotations;

import com.nikitiuk.javabeansinitializer.annotations.testbeans.ControllerBean;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanDependent;
import com.nikitiuk.javabeansinitializer.annotations.testbeans.TestBeanIndependent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

public class ContextInitializerTest {

    private static Logger logger = LoggerFactory.getLogger(ContextInitializerTest.class);
    private static ApplicationCustomContext applicationCustomContext;

    @BeforeAll
    private static void initialize() {
        applicationCustomContext = new ContextInitializer().initializeContext("com.nikitiuk.javabeansinitializer.annotations");
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