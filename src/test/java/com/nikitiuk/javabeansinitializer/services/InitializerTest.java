package com.nikitiuk.javabeansinitializer.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {
    private static Logger logger;

    @BeforeAll
    public static void setLogger() throws Exception
    {
        logger = LoggerFactory.getLogger(Converter.class);
    }
    @Test
    void initializeBeansTest() {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beansPerson.xml";
            Map<String,Object> beans = Initializer.initializeBeans(Reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml));
            Object bean = beans.get("executor");
            assertEquals("Executor", bean.getClass().getDeclaredMethod("getName").invoke(bean), "Name is Not \"Executor\"");
            assertEquals("ExecutorOfThings", bean.getClass().getDeclaredMethod("getJob").invoke(bean), "Job Name is Not Equal \"ExecutorOfThings\"");
            assertNull(bean.getClass().getDeclaredMethod("getSalary").invoke(bean), "Salary is Not Null");
            assertEquals(beans.get("eBoss"), bean.getClass().getDeclaredMethod("getBoss").invoke(bean), "Boss is Not \"eBoss\"");
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }
}