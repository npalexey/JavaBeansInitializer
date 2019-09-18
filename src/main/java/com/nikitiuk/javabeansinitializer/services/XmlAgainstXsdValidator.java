package com.nikitiuk.javabeansinitializer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlAgainstXsdValidator {

    private static final Logger logger = LoggerFactory.getLogger(XmlAgainstXsdValidator.class);

    public static boolean validateXMLSchema(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            /*File xsdFile = new File(xsdPath);
            File xmlFile = new File(xmlPath);
            if(!xsdFile.exists() || !xmlFile.exists()) {
                return false;
            }*/
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return true;
        } catch (IOException | SAXException e) {
            logger.error("Error at XmlAgainstXsdValidator validateXMLSchema.", e);
            return false;
        }
    }
}