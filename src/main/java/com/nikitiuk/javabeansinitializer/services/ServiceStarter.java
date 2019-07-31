package com.nikitiuk.javabeansinitializer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class ServiceStarter {
    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);

    public static void main(String[] args) {
        try{
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beansPerson.xml";
            Map<String, Object> beans = Initializer.initializeBeans(Reader.readXmlAndGetXmlCollectedBeans(expression, pathToXml));
            logger.info(beans.toString());
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }
}
