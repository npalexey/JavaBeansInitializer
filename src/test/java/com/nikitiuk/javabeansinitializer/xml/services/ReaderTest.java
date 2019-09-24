package com.nikitiuk.javabeansinitializer.xml.services;

import com.nikitiuk.javabeansinitializer.xml.collections.XmlCollectedBeans;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReaderTest {

    private static final Logger logger = LoggerFactory.getLogger(ReaderTest.class);
    private static final String BEANS_EXPRESSION = "/beans/*";
    private static final String PATH_TO_XML = "src/main/resources/beans.xml";

    @Test
    public void parseXmlFileIntoNodeListByCertainExpressionTest() throws Exception {
        Reader reader = new Reader();
        NodeList nodeList = reader.parseXmlFileIntoNodeListByCertainExpression(BEANS_EXPRESSION, PATH_TO_XML);
        logger.info(Integer.toString(nodeList.getLength()));
        assertEquals(5, nodeList.getLength());
    }

    @Test
    public void getXmlCollectedBeansFromNodeListTest() throws Exception {
        Reader reader = new Reader();
        NodeList nodeList = reader.parseXmlFileIntoNodeListByCertainExpression(BEANS_EXPRESSION, PATH_TO_XML);
        XmlCollectedBeans xmlCollectedBeans = reader.getXmlCollectedBeansFromNodeList(nodeList);
        logger.info(xmlCollectedBeans.toString());
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("init"));
        assertTrue(xmlCollectedBeans.getMainMethodMap().containsValue("executor"));
        assertEquals("executor", xmlCollectedBeans.getBeanCollectionsMap().get("Bean №1").getAttributesMap().get("id"));
    }

    @Test
    public void readXmlAndGetXmlCollectedBeansTest() throws Exception {
        Reader reader = new Reader();
        XmlCollectedBeans xmlCollectedBeans = reader.readXmlAndGetXmlCollectedBeans(BEANS_EXPRESSION, PATH_TO_XML);
        assertEquals(2, xmlCollectedBeans.getBeanCollectionsMap().size());
    }
}