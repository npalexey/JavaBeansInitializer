package com.nikitiuk.javabeansinitializer.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class XmlAgainstXsdValidatorTest {
    private static Logger logger = LoggerFactory.getLogger(XmlAgainstXsdValidator.class);

    @Test
    public void validateXMLSchemaTestForValidXML() {
        String xmlSource = "src/main/resources/beans.xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info("Test validator with valid XML file: " + xmlSource + "; against valid XSD: " + xsdSource);
        assertTrue(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }
    @Test
    public void validateXMLSchemaTestForInvalidXML() {
        String xmlSource = "src/main/resources/beans(invalid).xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info("Test validator with invalid XML file: " + xmlSource + "; against valid XSD: " + xsdSource);
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }
    @Test
    public void validateXMLSchemaTestForInvalidPath(){
        String xmlSource = "src/main/resources/beansS.xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info("Test validator with invalid path to Xml file: " + xmlSource + "; against valid XSD: " + xsdSource);
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }
}