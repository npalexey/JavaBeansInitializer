package com.nikitiuk.javabeansinitializer.xml.services;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlAgainstXsdValidatorTest {

    private static final Logger logger = LoggerFactory.getLogger(XmlAgainstXsdValidatorTest.class);

    @Test
    public void validateXMLSchemaTestForValidXML() {
        String xmlSource = "src/main/resources/beans.xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info(String.format("Test validator with valid XML file: %s; against valid XSD: %s.", xmlSource, xsdSource));
        assertTrue(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }

    @Test
    public void validateXMLSchemaTestForInvalidXML() {
        String xmlSource = "src/main/resources/beans(invalid).xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info(String.format("Test validator with invalid XML file: %s; against valid XSD: %s.", xmlSource, xsdSource));
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }

    @Test
    public void validateXMLSchemaTestForInvalidPath() {
        String xmlSource = "src/main/resources/beansS.xml";
        String xsdSource = "src/main/resources/beans.xsd";
        logger.info(String.format("Test validator with invalid path to Xml file: %s; against valid XSD: %s.", xmlSource, xsdSource));
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, xsdSource));
    }
}