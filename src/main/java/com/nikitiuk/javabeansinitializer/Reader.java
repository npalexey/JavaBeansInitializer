package com.nikitiuk.javabeansinitializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Reader {
    private static final Logger logger =  LoggerFactory.getLogger(XmlAgainstXsdValidator.class);
    public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = parseXmlFileInNodeListByCertainExpression(expression, pathToXml);
            getAttributesFromNodeList(nodeList);
            Map<String, Map<String, String>> beansMap = new HashMap<>();
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    public static NodeList parseXmlFileInNodeListByCertainExpression(String expression, String pathToXml) throws Exception{
        try {
            FileInputStream fileIS = new FileInputStream(new File(pathToXml));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            XPath xPath = XPathFactory.newInstance().newXPath();
            Document xmlDocument = builder.parse(fileIS);
            return (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        } catch (ParserConfigurationException|SAXException|IOException|XPathExpressionException e) {
            logger.error("Exception: "+e.getMessage());
            throw e;
        }
    }

    public static void getAttributesFromNodeList(NodeList nodeList){
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            /*if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                logger.info(element.toString());
            }*/
            logger.info(node.getNodeName());
            NamedNodeMap attributeMap = node.getAttributes();
            for (int j = 0; j < attributeMap.getLength(); ++j) {
                logger.info(attributeMap.item(j).toString());
            }
        }
    }
}