package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    private static Logger logger;

    @BeforeAll
    public static void setLogger() throws Exception
    {
        logger = LoggerFactory.getLogger(Reader.class);
    }

    @Test
    void parseXmlFileIntoNodeListByCertainExpressionTest() {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            logger.info(Integer.toString(nodeList.getLength()));
            assertEquals(2,nodeList.getLength());
        } catch (Exception e) {
            logger.error("Exception caught: " + e);
        }
    }

    @Test
    void getXmlCollectedBeansFromNodeListTest() {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            //logger.info(getAttributesFromNodeList(nodeList).toString());
            XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
            logger.info(xmlCollectedBeans.toString());
        } catch (Exception e) {
            logger.error("Exception caught: " + e);
        }
    }
}