package com.nikitiuk.javabeansinitializer.collections;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class BeanMapper {

    private Map<String, String> attributesMap;
    private Map<String, Map<String, String>> propertiesMap;

    public BeanMapper() {
    }

    public String toString() {
        if (attributesMap != null || propertiesMap != null) {
            return String.format("Attributes Map: %s Properties Map: %s",
                    StringUtils.defaultIfBlank(Objects.toString(attributesMap, null), "no attributes."),
                    StringUtils.defaultIfBlank(Objects.toString(propertiesMap, null), "no properties."));
            /*return "Attributes Map: " + attributesMap.toString() + " Properties Map: " + propertiesMap.toString();
        } else if (attributesMap != null){
            return "Attributes Map: " + attributesMap.toString();*/
        } else {
            return "Bean collection is empty";
        }
    }

    public Map getAttributesMap() {
        return attributesMap;
    }

    public void setAttributesMap(Map<String, String> attributesMap) {
        this.attributesMap = attributesMap;
    }

    public Map getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, Map<String, String>> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }
}