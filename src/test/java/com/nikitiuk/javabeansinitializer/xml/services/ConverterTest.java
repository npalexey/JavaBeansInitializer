package com.nikitiuk.javabeansinitializer.xml.services;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterTest {

    private static final Logger logger = LoggerFactory.getLogger(ConverterTest.class);
    private static final List<String> testList = Arrays.asList("34", "34.5", "false", "true", "null", "asf212nullfalse4");

    @Test
    public void convertAndGetValueTest() {
        List<Object> valuesList = new ArrayList<>();
        for (String el : testList) {
            Object value = Converter.convertAndGetValue(el);
            if (value == null) {
                logger.info("null");
            } else {
                logger.info(value.toString());
            }
            valuesList.add(value);
        }

        assertEquals(6, valuesList.size());
        assertEquals(34, valuesList.get(0));
        assertEquals(Double.class, valuesList.get(1).getClass());
        assertFalse((Boolean) valuesList.get(2));
        assertTrue((Boolean) valuesList.get(3));
        assertNull(valuesList.get(4));
        assertEquals("asf", valuesList.get(5).toString().substring(0, 3));
    }

    @Test
    public void getTypeOfVariableTest() {
        List<Class<?>> typesList = new ArrayList<>();
        for (String el : testList) {
            if (Converter.getTypeOfVariable(el) == null) {
                logger.info("null");
            } else {
                Class<?> type = Converter.getTypeOfVariable(el);
                logger.info(type.toString());
                typesList.add(type);
            }
        }
        assertEquals(6, typesList.size());
        assertEquals(Integer.class, typesList.get(0));
        assertEquals(Double.class, typesList.get(1));
        assertEquals(Boolean.class, typesList.get(2));
        assertEquals(Boolean.class, typesList.get(3));
        assertEquals(Object.class, typesList.get(4));
        assertEquals(String.class, typesList.get(5));
    }

    @Test
    public void convertAndGetTypePlusValueTest() {
        Map<Object, Class<?>> convertMap = new HashMap<>();
        for (String el : testList) {
            convertMap.putAll(Converter.convertAndGetTypePlusValue(el));
        }
        logger.info(convertMap.toString());
        assertTrue(convertMap.containsKey((int) 34.6433));
        assertEquals(6, convertMap.size());
    }
}