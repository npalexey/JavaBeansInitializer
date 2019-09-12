package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {

    private static Logger logger = LoggerFactory.getLogger(Reader.class);

    @Test
    public void parseXmlFileIntoNodeListByCertainExpressionTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        Reader reader = new Reader();
        NodeList nodeList = reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
        logger.info(Integer.toString(nodeList.getLength()));
        assertEquals(5, nodeList.getLength());
    }

    @Test
    public void getXmlCollectedBeansFromNodeListTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        Reader reader = new Reader();
        NodeList nodeList = reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
        XmlCollectedBeans xmlCollectedBeans = reader.getXmlCollectedBeansFromNodeList(nodeList);
        logger.info(xmlCollectedBeans.toString());
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("init"));
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("executor"));
        assertEquals("executor", xmlCollectedBeans.getBeanCollectionsMap().get("Bean №1").getAttributesMap().get("id"));
    }

    @Test
    public void readXmlAndGetXmlCollectedBeansTest() throws Exception {
        String expression = "/beans/*";
        String pathToXml = "src/main/resources/beans.xml";
        Reader reader = new Reader();
        XmlCollectedBeans xmlCollectedBeans = reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml);
        assertEquals(2, xmlCollectedBeans.getBeanCollectionsMap().size());
    }
}