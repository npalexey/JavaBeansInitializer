package com.nikitiuk.javabeansinitializer.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    private static Logger logger;

    @BeforeAll
    public static void setLogger() throws Exception
    {
        logger = LoggerFactory.getLogger(Converter.class);
    }

    @Test
    void convertAndGetValueTest() {
        /*List<String> testList = new ArrayList<>();
        testList.add("34");
        testList.add("34.5");
        testList.add("false");
        testList.add("true");
        testList.add("null");
        testList.add("asfaf212nullfalse4");
        List<?> typesList = new ArrayList<>();
        for (String el: testList) {
            if(Converter.convertAndGetValue(el) == null){
                logger.info("null");
            } else {
                Object type = Converter.convertAndGetValue(el);
                logger.info(type.toString());
                typesList.add(type);
            }
        }
        assertEquals(typesList.size(), 6);
        assertEquals(typesList.get(0), Integer.class);
        assertEquals(typesList.get(1), Double.class);
        assertEquals(typesList.get(2), Boolean.class);
        assertEquals(typesList.get(3), Boolean.class);
        assertEquals(typesList.get(4), Object.class);
        assertEquals(typesList.get(5), String.class);*/
    }

    @Test
    void getTypeOfVariableTest() {
        List<String> testList = new ArrayList<>();
        testList.add("34");
        testList.add("34.5");
        testList.add("false");
        testList.add("true");
        testList.add("null");
        testList.add("asfaf212nullfalse4");
        List<Class<?>> typesList = new ArrayList<>();
        for (String el: testList) {
            if(Converter.getTypeOfVariable(el) == null){
                logger.info("null");
            } else {
                Class<?> type = Converter.getTypeOfVariable(el);
                logger.info(type.toString());
                typesList.add(type);
            }
        }
        assertEquals(typesList.size(), 6);
        assertEquals(typesList.get(0), Integer.class);
        assertEquals(typesList.get(1), Double.class);
        assertEquals(typesList.get(2), Boolean.class);
        assertEquals(typesList.get(3), Boolean.class);
        assertEquals(typesList.get(4), Object.class);
        assertEquals(typesList.get(5), String.class);
    }

    @Test
    void convertAndGetTypePlusValueTest() {
    }
}