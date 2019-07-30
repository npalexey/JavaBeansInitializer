package com.nikitiuk.javabeansinitializer.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.nikitiuk.javabeansinitializer.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Reader {
    private static final Logger logger =  LoggerFactory.getLogger(XmlAgainstXsdValidator.class);
    /*public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            //logger.info(getAttributesFromNodeList(nodeList).toString());
            XmlCollectedBeans xmlCollectedBeans = getXmlCollectedBeansFromNodeList(nodeList);
            logger.info(xmlCollectedBeans.toString());
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }*/

    public static NodeList parseXmlFileIntoNodeListByCertainExpression(String expression, String pathToXml) throws Exception{
        try {
            FileInputStream fileIS = new FileInputStream(new File(pathToXml));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            XPath xPath = XPathFactory.newInstance().newXPath();
            Document xmlDocument = builder.parse(fileIS);
            return (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        } catch (ParserConfigurationException|SAXException|IOException|XPathExpressionException e) {
            logger.error("Exception caught: "+e.getMessage());
            throw e;
        }
    }

    public static XmlCollectedBeans getXmlCollectedBeansFromNodeList(NodeList nodeList) throws Exception{
        if(nodeList == null){
            logger.error("Error: Node list is empty");
            throw new NullPointerException();
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

        //logger.info(importsList.toString());
        //logger.info(mainMethodMap.toString());
        xmlCollectedBeans.setImports(importsList);
        xmlCollectedBeans.setMainMethodMap(mainMethodMap);
        xmlCollectedBeans.setBeanCollectionsMap(beanCollectionsMap);
        return xmlCollectedBeans;
    }

    private static BeanMapper getBeanAndItsProperties(Node node) throws Exception{
        if(node == null){
            logger.error("Error: Node equals null");
            throw new NullPointerException();
        }

        BeanMapper beanMapper = new BeanMapper();

        beanMapper.setAttributesMap(getAttributesOfBean(node));

        if(node.hasChildNodes()){
            beanMapper.setPropertiesMap(getPropertiesOfBean(node));
        }

        return beanMapper;
    }

    private static Map<String, String> getAttributesOfBean(Node node){
        Map<String,String> attributeMap = new HashMap<>();
        NamedNodeMap namedNodeMap = node.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); i++){
            attributeMap.put(namedNodeMap.item(i).getNodeName(), namedNodeMap.item(i).getTextContent());
        }
        //logger.info(attributeMap.toString());
        return attributeMap;
    }

    private static Map<String, Map<String, String>> getPropertiesOfBean(Node node){
        Map<String, Map<String, String>> propertiesMap = new HashMap<>();
        int propNumber = 1;
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); ++i) {
            Node childNode = childNodeList.item(i);
            if (childNode.hasAttributes()){
                NamedNodeMap namedChildNodeMap = childNode.getAttributes();
                Map<String,String> propMap = new HashMap<>();
                for (int j = 0; j < namedChildNodeMap.getLength(); ++j) {
                    propMap.put(namedChildNodeMap.item(j).getNodeName(), namedChildNodeMap.item(j).getTextContent());
                }
                propertiesMap.put("Property №"+propNumber, propMap);
                propNumber++;
            }
        }
        //logger.info(propertiesMap.toString());
        return propertiesMap;
    }

    /*public static ArrayList<String> getImportsFromNodeList(NodeList nodeList) throws Exception{
        if(nodeList == null){
            logger.info("Error: Node list is empty");
            throw new NullPointerException();
        }

        ArrayList<String> importsList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if(node.getNodeName().equals("import")){
                importsList.add(node.getAttributes().getNamedItem("src").getTextContent());
            }
        }
        logger.info(importsList.toString());
        return importsList;
    }

    public static Map<String, String> getMainFromNodeList(NodeList nodeList) throws Exception{
        if(nodeList == null){
            logger.info("Error: Node list is empty");
            throw new NullPointerException();
        }

        Map<String, String> mainMap = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if(node.getNodeName().equals("main")){
                mainMap.put(node.getAttributes().getNamedItem("bean").getTextContent(), node.getAttributes().getNamedItem("method").getTextContent());
            }
        }

        logger.info(mainMap.toString());
        return mainMap;
    }

    public static ArrayList<BeanCollection> getBeanCollectionsFromNodeList(NodeList nodeList) throws Exception{
        if(nodeList == null){
            logger.info("Error: Node list is empty");
            throw new NullPointerException();
        }

        ArrayList<BeanCollection> beanCollections = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if(node.getNodeName().equals("bean")){
                beanCollections.add(getBeanAndItsProperties(node));
            }
        }
        return beanCollections;
    }*/

   /* public static Map<String, Map<String, ArrayList<String>>> getAttributesFromNodeList(NodeList nodeList) throws Exception{
        if(nodeList == null){
            logger.info("Error: Node list is empty");
            throw new NullPointerException();
        }
        Map<String, Map<String, ArrayList<String>>> dataMap = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);*/
            /*if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                logger.info(element.toString());
            }*/
            //logger.info(node.getNodeName());

           /* String mainMapKey = node.getNodeName()+"s";
            if (dataMap.containsKey(mainMapKey)){
                dataMap.replace(mainMapKey, transformXmlDataFromCertainNodeIntoMap(node, dataMap.get(mainMapKey)));
            } else {
                dataMap.put(mainMapKey, transformXmlDataFromCertainNodeIntoMap(node));
            }*/
            /*NamedNodeMap attributeMap = node.getAttributes();
            for (int j = 0; j < attributeMap.getLength(); ++j) {
                //logger.info(attributeMap.item(j).toString());
                //logger.info(attributeMap.item(j).getNodeName());
                //logger.info(attributeMap.item(j).getTextContent());
            }*/
        /*}
        return dataMap;
    }

    public static Map<String,ArrayList<String>> transformXmlDataFromCertainNodeIntoMap(Node node){
        NamedNodeMap attributeMap = node.getAttributes();
        Map<String, ArrayList<String>> nodeValues = new HashMap<>();
        ArrayList<String> arrayListWithValues = new ArrayList<>();
        for (int j = 0; j < attributeMap.getLength(); ++j) {
            //logger.info(attributeMap.item(j).toString());
            arrayListWithValues.add(attributeMap.item(j).getTextContent());
            nodeValues.put(attributeMap.item(j).getNodeName(), arrayListWithValues);
        }
        return nodeValues;
    }

    public static Map<String,ArrayList<String>> transformXmlDataFromCertainNodeIntoMap(Node node, Map<String, ArrayList<String>> valuesMap) throws Exception{
        NamedNodeMap attributeMap = node.getAttributes();

        for (int j = 0; j < attributeMap.getLength(); ++j) {*/
            //logger.info(attributeMap.item(j).toString());
            /*if(valuesMap.get(attributeMap.item(j).getNodeName()) == null){
                logger.info("Error: There Is No ArrayList");
                logger.info(attributeMap.item(j).getNodeName());
                for (int i = 0; i < attributeMap.getLength(); ++i) {
                    logger.info(attributeMap.item(i).toString());
                    logger.info(attributeMap.item(i).getNodeName());
                    logger.info(attributeMap.item(i).getTextContent());
                }
                logger.info(valuesMap.toString());
                throw new NullPointerException();
            }*/
            /*if (valuesMap.get(attributeMap.item(j).getNodeName()) != null){
                ArrayList<String> updatedArrayList = valuesMap.get(attributeMap.item(j).getNodeName());
                updatedArrayList.add(attributeMap.item(j).getTextContent());
                valuesMap.put(attributeMap.item(j).getNodeName(), updatedArrayList);
            } else {
                ArrayList<String> arrayListOfValues = new ArrayList<>();
                arrayListOfValues.add(attributeMap.item(j).getTextContent());
                valuesMap.put(attributeMap.item(j).getNodeName(), arrayListOfValues);
            }

        }
        return valuesMap;
    }*/
}