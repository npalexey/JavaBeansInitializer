package com.nikitiuk.javabeansinitializer.xml.services;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlAgainstXsdValidatorTest {

    private static final Logger logger = LoggerFactory.getLogger(XmlAgainstXsdValidatorTest.class);
    private static final String XSD_SOURCE = "src/main/resources/beans.xsd";

    @Test
    public void validateXMLSchemaTestForValidXML() {
        String xmlSource = "src/main/resources/beans.xml";
        logger.info(String.format("Test validator with valid XML file: %s; against valid XSD: %s.", xmlSource, XSD_SOURCE));
        assertTrue(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, XSD_SOURCE));
    }

    @Test
    public void validateXMLSchemaTestForInvalidXML() {
        String xmlSource = "src/main/resources/beans(invalid).xml";
        logger.info(String.format("Test validator with invalid XML file: %s; against valid XSD: %s.", xmlSource, XSD_SOURCE));
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, XSD_SOURCE));
    }

    @Test
    public void validateXMLSchemaTestForInvalidPath() {
        String xmlSource = "src/main/resources/beansS.xml";
        logger.info(String.format("Test validator with invalid path to Xml file: %s; against valid XSD: %s.", xmlSource, XSD_SOURCE));
        assertFalse(XmlAgainstXsdValidator.validateXMLSchema(xmlSource, XSD_SOURCE));
    }
}