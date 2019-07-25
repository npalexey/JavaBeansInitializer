package com.nikitiuk.javabeansinitializer;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class XmlAgainstXsdValidator {
    private static final Logger logger =  LoggerFactory.getLogger(XmlAgainstXsdValidator.class);

    public static void main(String[] args) {
        String xml = "src/main/resources/beans.xml";
        String xsd = "src/main/resources/beans.xsd";
        boolean isValid = validateXMLSchema(xsd,xml);
        if(isValid){
            logger.info("Xml" + " is VALID against " + "Xsd");
        } else {
            logger.info("Xml" + " is NOT valid against " + "Xsd");
        }
    }

    public static boolean validateXMLSchema(String xsdPath, String xmlPath){
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException e){
            logger.info("Exception: "+e.getMessage());
            return false;
        }catch(SAXException e1){
            logger.info("SAX Exception: "+e1.getMessage());
            return false;
        }

        return true;
    }
}