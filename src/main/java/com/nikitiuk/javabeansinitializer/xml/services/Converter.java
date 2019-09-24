package com.nikitiuk.javabeansinitializer.xml.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Converter {

    private static final Logger logger = LoggerFactory.getLogger(Converter.class);
    private static final String BOOLEAN_FALSE = "false";
    private static final String BOOLEAN_TRUE = "true";

    private Converter() {
        throw new IllegalStateException("Utility class");
    }

    public static Object convertAndGetValue(String s) {
        logger.debug(String.format("Converting to value %s", s));
        if (s.equals("null")) {
            return null;
        } else if (NumberUtils.isParsable(s)) {
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    return scanner.nextInt();
                } else {
                    return scanner.nextDouble();
                }
            }
        } else if (s.equals(BOOLEAN_TRUE) || s.equals(BOOLEAN_FALSE)) {
            return Boolean.parseBoolean(s);
        } else {
            return s;
        }
    }

    public static Class<?> getTypeOfVariable(String s) {
        logger.debug(String.format("Getting type of variable %s", s));
        if (s.equals("null")) {
            return Object.class;               //returning Object class, so we don't get a NPE
        } else if (NumberUtils.isParsable(s)) {
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    return Integer.class;
                } else {
                    return Double.class;
                }
            }
        } else if (s.equals(BOOLEAN_TRUE) || s.equals(BOOLEAN_FALSE)) {
            return Boolean.class;
        } else {
            return String.class;
        }

    }

    public static Map<Object, Class<?>> convertAndGetTypePlusValue(String s) {
        logger.debug(String.format("Getting type and converting to value variable %s", s));
        Map<Object, Class<?>> convMap = new HashMap<>();
        if (s.equals("null")) {
            convMap.put(null, Object.class);    //casting null to an Object type so that we don't get the NPE
        } else if (NumberUtils.isParsable(s)) {
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    convMap.put(scanner.nextInt(), Integer.class);
                } else {
                    convMap.put(scanner.nextDouble(), Double.class);
                }
            }
        } else if (s.equals(BOOLEAN_TRUE) || s.equals(BOOLEAN_FALSE)) {
            convMap.put(Boolean.parseBoolean(s), Boolean.class);
        } else {
            convMap.put(s, String.class);
        }
        return convMap;
    }
}