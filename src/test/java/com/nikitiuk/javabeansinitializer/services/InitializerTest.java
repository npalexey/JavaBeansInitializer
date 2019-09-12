package com.nikitiuk.javabeansinitializer.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class InitializerTest {

    private static Logger logger = LoggerFactory.getLogger(Converter.class);

    @Test
    public void initializeBeansTest() {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beansPerson.xml";
            Reader reader = new Reader();
            Initializer initializer = new Initializer();
            Map<String,Object> beans = initializer.initializeBeans(reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml));
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