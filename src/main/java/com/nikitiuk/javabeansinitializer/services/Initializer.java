package com.nikitiuk.javabeansinitializer.services;

import com.nikitiuk.javabeansinitializer.beans.Person;
import com.nikitiuk.javabeansinitializer.collections.BeanMapper;
import com.nikitiuk.javabeansinitializer.collections.XmlCollectedBeans;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Initializer {
    private static final Logger logger =  LoggerFactory.getLogger(Initializer.class);

    public static void main(String[] args) {
        try {
            String expression = "/beans/*";
            String pathToXml = "src/main/resources/beans.xml";
            NodeList nodeList = Reader.parseXmlFileIntoNodeListByCertainExpression(expression, pathToXml);
            XmlCollectedBeans xmlCollectedBeans = Reader.getXmlCollectedBeansFromNodeList(nodeList);
            initialize(xmlCollectedBeans);
            //instantiate(xmlCollectedBeans.getBeanCollectionsMap().get("Bean №" + 1), "com.nikitiuk.javabeansinitializer.beans.Person");
        } catch (Exception e){
            logger.error("Exception caught: " + e);
        }
    }

    public static void initialize(XmlCollectedBeans beans) throws Exception{
        if(beans == null){
            logger.error("No beans were passed to 'initialize' method");
            throw new NullPointerException();
        }
        Map<String, BeanMapper> beansMap = beans.getBeanCollectionsMap();
        for(int i = 1; i < beansMap.size(); i++){
            logger.info(beansMap.get("Bean №" + i).toString());
        }
    }

    public static void createObjectFromBeanData(XmlCollectedBeans xmlCollectedBeans) throws Exception{
        Map<String, Object> beanObjects = new HashMap<>();
        for(Map.Entry<String, BeanMapper> mapperEntry : xmlCollectedBeans.getBeanCollectionsMap().entrySet()){
            try {
                //unchecked cast:
                Class<?> beanClass = Class.forName(mapperEntry.getValue().getAttributesMap().get("class").toString());
                Object bean = beanClass.getDeclaredConstructor().newInstance();
                insertBeanProperties(mapperEntry.getValue().getPropertiesMap(), bean);
                //BeanUtils.populate(bean, );
            }
            catch (Exception ex){
                logger.error(ex + " Cannot instantiate Interpreter.");
                logger.error("Check class name, class path.");
            }

            Object bean = new Object[mapperEntry.getValue().getPropertiesMap().size()];
            //mapperEntry.getValue().getPropertiesMap()
        }
    }

    public static void insertBeanProperties(Map<String,Map<String,String>> propertiesMap, Object bean){
        for (Map.Entry<String,Map<String,String>> entry: propertiesMap.entrySet()){
            String propName = entry.getValue().get("name");
            try {
                Class<?> fieldType = bean.getClass().getDeclaredField(propName).getType();
                PropertyDescriptor descriptor = new PropertyDescriptor(propName, fieldType);
            } catch (Exception e){
                logger.error("Exception caught: " + 3);
            }
        }
    }

    public static void insertBeanPropertiesWithPopulate(Object bean, Map<String, Map<String,String>> propertiesMap){
        Map<String, Object> propMap2 = new HashMap<>();
        for (Map.Entry<String,Map<String,String>> entry: propertiesMap.entrySet()){
            if(entry.getValue().containsKey("value")){
                propMap2.put(entry.getValue().get("name"), Converter.convertAndGetValue(entry.getValue().get("value")));
            }
        }
    }

    public static void getBeanFromMap(Map<String, BeanMapper> beansMap, int beanNumber){
        beansMap.get("Bean №" + beanNumber);
    }

    public static Object instantiate(List<String> args, String className) throws Exception {
        // Load the class.
        Class<?> clazz = Class.forName(className);

        // Search for an "appropriate" constructor.
        for (Constructor<?> ctor : clazz.getConstructors()) {
            Class<?>[] paramTypes = ctor.getParameterTypes();

            // If the arity matches, let's use it.
            if (args.size() == paramTypes.length) {

                // Convert the String arguments into the parameters' types.
                Object[] convertedArgs = new Object[args.size()];
                for (int i = 0; i < convertedArgs.length; i++) {
                    //convertedArgs[i] = convert(paramTypes[i], args.get(i));
                }

                // Instantiate the object with the converted arguments.
                return ctor.newInstance(convertedArgs);
            }
        }

        throw new IllegalArgumentException("Don't know how to instantiate " + className);
    }

}
