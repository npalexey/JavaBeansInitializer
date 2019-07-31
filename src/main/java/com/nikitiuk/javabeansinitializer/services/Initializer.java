package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Initializer {
    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);

    public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beansPerson.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
            Map<String,Object> beans = createObjectsFromBeanData(xmlCollectedBeans);
            //logger.info(beans.toString());
            loopToChangeNowUsedForWiring(beans, xmlCollectedBeans);
            logger.info(xmlCollectedBeans.toString());
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }

    public static Map<String, Object> createObjectsFromBeanData(XmlCollectedBeans xmlCollectedBeans) throws Exception{
        Map<String, Object> beanObjects = new HashMap<>();
        for(Map.Entry<String, BeanMapper> mapperEntry : xmlCollectedBeans.getBeanCollectionsMap().entrySet()){
            try {
                Class<?> beanClass = Class.forName(mapperEntry.getValue().getAttributesMap().get("class").toString());
                Object bean = insertBeanProperties(mapperEntry.getValue().getPropertiesMap(), beanClass.getDeclaredConstructor().newInstance());
                //List<String> idAndNumber = new ArrayList<>();
                //idAndNumber.add(0, mapperEntry.getValue().getAttributesMap().get("id").toString());
                //idAndNumber.add(1, mapperEntry.getKey());
                beanObjects.put(mapperEntry.getValue().getAttributesMap().get("id").toString(), bean);
            }
            catch (Exception ex){
                logger.error("Exception caught: " + ex + " Cannot instantiate bean.");
            }
        }
        return beanObjects;
    }

    private static Object insertBeanProperties(Map<String,Map<String,String>> propertiesMap, Object bean) throws Exception{
        try {
            Object beanUpd = bean.getClass().newInstance();
            for (Map.Entry<String,Map<String,String>> entry: propertiesMap.entrySet()){
                String propName = entry.getValue().get("name");
                Class<?> fieldType = bean.getClass().getDeclaredField(propName).getType();
                /*if(entry.getValue().containsKey("value")){
                    Class<?> fieldTypeToCheck = Converter.getTypeOfVariable(entry.getValue().get("value"));
                    logger.info("Field Type of Person: " + fieldType.toString() + " vs Field Type to Check: " + fieldTypeToCheck.toString());
                }*/
                if(entry.getValue().containsKey("value") && fieldType == Converter.getTypeOfVariable(entry.getValue().get("value"))){
                    Field field = bean.getClass().getDeclaredField(entry.getValue().get("name"));
                    field.setAccessible(true);
                    field.set(beanUpd, Converter.convertAndGetValue(entry.getValue().get("value")));
                }
            }
            //logger.info(beanUpd.toString());
            return beanUpd;
        } catch (Exception e){
            logger.error("Exception caught: " + e);
            throw e;
        }
    }

    public static void loopToChangeNowUsedForWiring(Map<String, Object> initializedBeans, XmlCollectedBeans xmlCollectedBeans){
        for(Map.Entry<String, BeanMapper> mapperEntry : xmlCollectedBeans.getBeanCollectionsMap().entrySet()){
            wireBeans(initializedBeans, initializedBeans.get(mapperEntry.getValue().getAttributesMap().get("id")), mapperEntry.getValue().getPropertiesMap());
        }
    }

    private static void wireBeans(Map<String, Object> initializedBeans, Object beanWhereToInsertWiring, Map<String, Map<String,String>> propertiesMapOfBeanWhereToInsertWiring){
        for(Map.Entry<String,Map<String,String>> propertyEntry : propertiesMapOfBeanWhereToInsertWiring.entrySet()){
            if(propertyEntry.getValue().containsKey("ref")){
                wire(initializedBeans.get(propertyEntry.getValue().get("ref")), beanWhereToInsertWiring, propertyEntry.getValue().get("name"));
            }
        }
    }

    private static void wire(Object beanToWire, Object beanWhereToInsertWiring, String wiringPropertyName){
        try{
            String capitalizedFirst = wiringPropertyName.substring(0,1).toUpperCase();
            String others = wiringPropertyName.substring(1);
            Method setWiring = beanWhereToInsertWiring.getClass().getDeclaredMethod("set"+capitalizedFirst+others, beanToWire.getClass());
            setWiring.invoke(beanWhereToInsertWiring, beanToWire);
        } catch (Exception e){
            logger.error("Exception caught while wiring beans: " + e);
        }
    }
}
