package com.nikitiuk.javabeansinitializer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class ServiceStarter {
    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);
    private static String expression = "/beans/*";

    public static String getExpression() {
        return expression;
    }

    public static void setExpression(String expression) {
        ServiceStarter.expression = expression;
    }

    public static void main(String[] args) {
        try{
            String pathToXml = "src/main/resources/beansPerson.xml";
            String pathToXsd = "src/main/resources/beans.xsd";
            Map<String, Object> beans = getBeansFromXMLValidatingItBeforehand(pathToXml, pathToXsd);
            logger.info(beans.toString());
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }

    public static Map<String, Object> getBeansFromXMLValidatingItBeforehand(String pathToXml, String pathToXsd) throws Exception{
        if(XmlAgainstXsdValidator.validateXMLSchema(pathToXml, pathToXsd)) {
            logger.info("Xml file is valid against Xsd");
            return getBeansFromXML(pathToXml);
        } else {
            logger.error("Xml file is not valid against Xsd");
            throw new Exception();
        }
    }

    public static Map<String, Object> getBeansFromXML(String pathToXml) throws Exception{
        try {
            return Initializer.initializeBeans(Reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml));
        } catch (Exception e){
            logger.error("Exception caught: " + e);
            throw e;
        }
    }
}
