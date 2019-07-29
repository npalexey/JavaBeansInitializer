package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import java.util.Map;

public class Initializer {
    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);

    public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
            initialize(xmlCollectedBeans);
        } catch (Exception e){
            logger.error("Exception: " + e);
        }
    }

    public static void initialize(XmlCollectedBeans beans) throws Exception{
        if(beans == null){
            logger.error("No beans were passed to 'initialize' method");
            throw new NullPointerException();
        }
        Map<String, BeanMapper> beansMap = beans.getBeanCollectionsList();
        for(int i = 1; i < beansMap.size(); i++){
            logger.info(beansMap.get("Bean №" + i).toString());
        }
    }

    public static void getBeanFromMap(Map<String, BeanMapper> beansMap, int beanNumber){
        beansMap.get("Bean №" + beanNumber);
    }
}
