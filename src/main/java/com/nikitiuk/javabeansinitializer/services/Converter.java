package com.nikitiuk.javabeansinitializer.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class Converter {
    private static final Logger logger =  LoggerFactory.getLogger(XmlAgainstXsdValidator.class);

    public static Object convertAndGetValue(String s){
        if (s.equals("null")) {
            return (Object)null;
        } else if (NumberUtils.isParsable(s)){
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    return scanner.nextInt();
                } else {
                    return scanner.nextDouble();
                }
            }
        } else if (s.equals("true") || s.equals("false")){
            return Boolean.parseBoolean(s);
        } else {
            return s;
        }
    }

    public static Class<?> getTypeOfVariable(String s){
        if (s.equals("null")) {
            return Object.class;
        } else if (NumberUtils.isParsable(s)){
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    return Integer.class;
                } else {
                    return Double.class;
                }
            }
        } else if (s.equals("true") || s.equals("false")){
            return Boolean.class;
        } else {
            return String.class;
        }

    }

    public static Map<Class<?>, Object> convertAndGetTypePlusValue(String s){
        Map<Class<?>, Object> convMap = new HashMap<>();
        if (s.equals("null")) {
            convMap.put(Object.class, null);
        } else if (NumberUtils.isParsable(s)){
            try (Scanner scanner = new Scanner(s)) {
                if (scanner.hasNextInt()) {
                    convMap.put(Integer.TYPE, scanner.nextInt());
                } else {
                    convMap.put(Double.TYPE, scanner.nextDouble());
                }
            }
        } else if (s.equals("true") || s.equals("false")){
            convMap.put(Boolean.TYPE, Boolean.parseBoolean(s));
        } else {
            convMap.put(String.class, s);
        }
        return convMap;
    }
}
