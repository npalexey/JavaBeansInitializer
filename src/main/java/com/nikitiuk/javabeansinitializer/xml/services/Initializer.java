package com.nikitiuk.javabeansinitializer.xml.services;

import com.nikitiuk.javabeansinitializer.xml.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.xml.collections.XmlCollectedBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Initializer {

    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);
    private static final String VALUE_STRING = "value";
    private static final String NAME_STRING = "name";
    private Reader reader = new Reader();

    public Map<String, Object> initializeBeans(XmlCollectedBeans xmlCollectedBeans) throws Exception{
        try {
            Map<String,Object> beans = createObjectsFromBeanData(xmlCollectedBeans);
            loopToChangeASAPThatIsNowUsedForWiring(beans, xmlCollectedBeans);
            initializeMainMethod(beans, xmlCollectedBeans);
            return beans;
        } catch (Exception e){
            logger.error("Error at Initializer initializeBeans.", e);
            throw e;
        }
    }

    private Map<String, Object> createObjectsFromBeanData(XmlCollectedBeans xmlCollectedBeans) throws Exception{
        Map<String, Object> beanObjects = new HashMap<>();
        if(xmlCollectedBeans.getImports() != null){
            beanObjects.putAll(getOtherBeansFromImports(xmlCollectedBeans));
        }
        for(Map.Entry<String, BeanMapper> mapperEntry : xmlCollectedBeans.getBeanCollectionsMap().entrySet()){
            try {
                Class<?> beanClass = Class.forName(mapperEntry.getValue().getAttributesMap().get("class").toString());
                Object bean = insertBeanProperties(mapperEntry.getValue().getPropertiesMap(), beanClass.getDeclaredConstructor().newInstance());
                beanObjects.put(mapperEntry.getValue().getAttributesMap().get("id").toString(), bean);
            }
            catch (Exception e){
                logger.error("Cannot instantiate bean. Error at Initializer createObjectsFromBeanData.", e);
            }
        }
        return beanObjects;
    }

    private Map<String, Object> getOtherBeansFromImports(XmlCollectedBeans xmlCollectedBeans) throws Exception{
        try{
            Map<String, Object> beanObjects = new HashMap<>();
            for(String importString : xmlCollectedBeans.getImports()){
                beanObjects.putAll(initializeBeans(reader.readXmlAndGetXmlCollectedBeans("/beans/*", importString)));
            }
            return beanObjects;
        } catch (Exception e){
            logger.error("Error while caught during importing additional beans.", e);
            throw e;
        }
    }

    private Object insertBeanProperties(Map<String,Map<String,String>> propertiesMap, Object bean) throws Exception{
        try {
            Object beanUpd = bean.getClass().getConstructor().newInstance();
            /*Object beanUpd = bean.getClass().newInstance();*/
            for (Map.Entry<String,Map<String,String>> entry: propertiesMap.entrySet()){
                String propName = entry.getValue().get(NAME_STRING);
                Class<?> fieldType = bean.getClass().getDeclaredField(propName).getType();
                /*if(entry.getValue().containsKey("value")){
                    Class<?> fieldTypeToCheck = Converter.getTypeOfVariable(entry.getValue().get("value"));                                   //debug code to check field types from
                    logger.info("Field Type of Person: " + fieldType.toString() + " vs Field Type to Check: " + fieldTypeToCheck.toString()); //xml and preset bean field types
                }*/
                if(entry.getValue().containsKey(VALUE_STRING) && fieldType == Converter.getTypeOfVariable(entry.getValue().get(VALUE_STRING))){
                    Field field = bean.getClass().getDeclaredField(entry.getValue().get(NAME_STRING));
                    field.setAccessible(true);
                    field.set(beanUpd, Converter.convertAndGetValue(entry.getValue().get(VALUE_STRING)));
                }
            }
            return beanUpd;
        } catch (Exception e){
            logger.error("Error at Initializer insertBeanProperties.", e);
            throw e;
        }
    }

    private void loopToChangeASAPThatIsNowUsedForWiring(Map<String, Object> initializedBeans, XmlCollectedBeans xmlCollectedBeans){
        for(Map.Entry<String, BeanMapper> mapperEntry : xmlCollectedBeans.getBeanCollectionsMap().entrySet()){
            wireBeans(initializedBeans, initializedBeans.get(mapperEntry.getValue().getAttributesMap().get("id")), mapperEntry.getValue().getPropertiesMap());
        }
    }

    private void wireBeans(Map<String, Object> initializedBeans, Object beanWhereToInsertWiring, Map<String, Map<String,String>> propertiesMapOfBeanWhereToInsertWiring){
        for(Map.Entry<String,Map<String,String>> propertyEntry : propertiesMapOfBeanWhereToInsertWiring.entrySet()){
            if(propertyEntry.getValue().containsKey("ref")){
                wire(initializedBeans.get(propertyEntry.getValue().get("ref")), beanWhereToInsertWiring, propertyEntry.getValue().get(NAME_STRING));
            }
        }
    }

    private void wire(Object beanToWire, Object beanWhereToInsertWiring, String wiringPropertyName){
        try{
            String capitalizedFirst = wiringPropertyName.substring(0,1).toUpperCase();
            String others = wiringPropertyName.substring(1);
            Method setWiring = beanWhereToInsertWiring.getClass().getDeclaredMethod("set"+capitalizedFirst+others, beanToWire.getClass());
            setWiring.invoke(beanWhereToInsertWiring, beanToWire);
        } catch (Exception e){
            logger.error("Error while wiring beans.", e);
        }
    }

    private void initializeMainMethod(Map<String, Object> beans, XmlCollectedBeans xmlCollectedBeans) {
        try{
            Map<String, String> mainMethodMap = xmlCollectedBeans.getMainMethodMap();
            Object beanWhoseMainMethodWillBeInitialized = beans.get(mainMethodMap.get("bean"));
            Method mainMethod = beanWhoseMainMethodWillBeInitialized.getClass().getDeclaredMethod(mainMethodMap.get("method"));
            mainMethod.invoke(beanWhoseMainMethodWillBeInitialized);
        } catch (Exception e){
            logger.error("Error while initializing main method.", e);
        }
    }
}