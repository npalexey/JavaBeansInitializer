package com.nikitiuk.javabeansinitializer.xml.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ServiceStarter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceStarter.class);
    private static String expression = "/beans/*";
    private static Initializer initializer = new Initializer();
    private static Reader reader = new Reader();

    public static void main(String[] args) {
        try {
            String pathToXml = "src/main/resources/beansPerson.xml";
            String pathToXsd = "src/main/resources/beans.xsd";
            Map<String, Object> beans = getBeansFromXMLValidatingItBeforehand(pathToXml, pathToXsd);
            logger.info(String.format("Initialized beans: %s", beans.toString()));
        } catch (Exception e) {
            logger.error("Exception caught at ServiceStarter main.", e);
        }
    }

    private static Map<String, Object> getBeansFromXMLValidatingItBeforehand(String pathToXml, String pathToXsd) throws Exception {
        if (XmlAgainstXsdValidator.validateXMLSchema(pathToXml, pathToXsd)) {
            logger.info("Xml file is valid against Xsd");
            return getBeansFromXML(pathToXml);
        } else {
            logger.info("Xml file is not valid against Xsd");
            return new HashMap<>();
        }
    }

    private static Map<String, Object> getBeansFromXML(String pathToXml) throws Exception {
        return initializer.initializeBeans(reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml));
    }
}