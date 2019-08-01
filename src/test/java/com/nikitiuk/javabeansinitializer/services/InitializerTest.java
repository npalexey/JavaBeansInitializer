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
            assertEquals(bean.getClass().getDeclaredMethod("getName").invoke(bean), "Executor", "Name is Not \"Executor\"");
            assertEquals(bean.getClass().getDeclaredMethod("getCityOfResidence").invoke(bean), "Odessa", "CoR is Not \"Odessa\"");
            assertEquals(bean.getClass().getDeclaredMethod("getJob").invoke(bean), "ExecutorOfThings", "Job Name is Not Equal \"ExecutorOfThings\"");
            assertNull(bean.getClass().getDeclaredMethod("getSalary").invoke(bean), "Salary is Not Null");
            assertEquals(bean.getClass().getDeclaredMethod("getBoss").invoke(bean), beans.get("eBoss"), "Boss is Not \"eBoss\"");
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }

    /*public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beansPerson.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
            Map<String,Object> beans = createObjectsFromBeanData(xmlCollectedBeans);
            loopToChangeNowUsedForWiring(beans, xmlCollectedBeans);
            initializeMainMethod(beans, xmlCollectedBeans);
            //logger.info(xmlCollectedBeans.toString());
            logger.info(beans.toString());
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }*/
}