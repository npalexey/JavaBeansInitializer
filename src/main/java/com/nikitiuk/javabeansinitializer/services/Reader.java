package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reader {

    private static final Logger logger = LoggerFactory.getLogger(XmlAgainstXsdValidator.class);

    public XmlCollectedBeans readXmlAndGetXmlCollectedBeans(String expression, String pathToXml) throws Exception {
        NodeList nodeList = parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
        return getXmlCollectedBeansFromNodeList(nodeList);
    }

    public NodeList parseXmlFileIntoNodeListByCertainExpression(String expression, String pathToXml) throws Exception {
        FileInputStream fileIS = new FileInputStream(new File(pathToXml));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        XPath xPath = XPathFactory.newInstance().newXPath();
        Document xmlDocument = builder.parse(fileIS);
        return (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

    }

    public XmlCollectedBeans getXmlCollectedBeansFromNodeList(NodeList nodeList) throws Exception {
        if (nodeList == null) {
            /*logger.error("Error: Node list is empty");*/
            throw new NotFoundException("Node list is empty");
        }

        XmlCollectedBeans xmlCollectedBeans = new XmlCollectedBeans();
        ArrayList<String> importsList = new ArrayList<>();
        Map<String, String> mainMethodMap = new HashMap<>();
        Map<String, BeanMapper> beanCollectionsMap = new HashMap<>();
        int beanNumber = 1;
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            switch (node.getNodeName()) {
                case "import":
                    importsList.add(node.getAttributes().getNamedItem("src").getTextContent());
                    break;
                case "main":
                    mainMethodMap.put("bean", node.getAttributes().getNamedItem("bean").getTextContent());
                    mainMethodMap.put("method", node.getAttributes().getNamedItem("method").getTextContent());
                    break;
                case "bean":
                    beanCollectionsMap.put("Bean №" + beanNumber, getBeanAndItsProperties(node));
                    beanNumber++;
                    break;
            }
        }

        xmlCollectedBeans.setImports(importsList);
        xmlCollectedBeans.setMainMethodMap(mainMethodMap);
        xmlCollectedBeans.setBeanCollectionsMap(beanCollectionsMap);
        return xmlCollectedBeans;
    }

    public BeanMapper getBeanAndItsProperties(Node node) throws Exception {
        if (node == null) {
            /*logger.error("Error: Node equals null");*/
            throw new NotFoundException("Node is empty.");
        }

        BeanMapper beanMapper = new BeanMapper();

        beanMapper.setAttributesMap(getAttributesOfBean(node));

        if (node.hasChildNodes()) {
            beanMapper.setPropertiesMap(getPropertiesOfBean(node));
        }

        return beanMapper;
    }

    public Map<String, String> getAttributesOfBean(Node node) {
        Map<String, String> attributeMap = new HashMap<>();
        NamedNodeMap namedNodeMap = node.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
            attributeMap.put(namedNodeMap.item(i).getNodeName(), namedNodeMap.item(i).getTextContent());
        }

        return attributeMap;
    }

    public Map<String, Map<String, String>> getPropertiesOfBean(Node node) {
        Map<String, Map<String, String>> propertiesMap = new HashMap<>();
        int propNumber = 1;
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); ++i) {
            Node childNode = childNodeList.item(i);
            if (childNode.hasAttributes()) {
                NamedNodeMap namedChildNodeMap = childNode.getAttributes();
                Map<String, String> propMap = new HashMap<>();
                for (int j = 0; j < namedChildNodeMap.getLength(); ++j) {
                    propMap.put(namedChildNodeMap.item(j).getNodeName(), namedChildNodeMap.item(j).getTextContent());
                }
                propertiesMap.put("Property №" + propNumber, propMap);
                propNumber++;
            }
        }

        return propertiesMap;
    }
}