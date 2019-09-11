package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    private static Logger logger = LoggerFactory.getLogger(Reader.class);

    @Test
    void parseXmlFileIntoNodeListByCertainExpressionTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
        logger.info(Integer.toString(nodeList.getLength()));
        assertEquals(5, nodeList.getLength());
    }

    @Test
    void getXmlCollectedBeansFromNodeListTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
        XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
        logger.info(xmlCollectedBeans.toString());
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("init"));
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("executor"));
        assertEquals("executor", xmlCollectedBeans.getBeanCollectionsMap().get("Bean №1").getAttributesMap().get("id"));
    }

    @Test
    void readXmlAndGetXmlCollectedBeansTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        XmlCollectedBeans xmlCollectedBeans = Reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml);
        assertEquals(2, xmlCollectedBeans.getBeanCollectionsMap().size());
    }
}