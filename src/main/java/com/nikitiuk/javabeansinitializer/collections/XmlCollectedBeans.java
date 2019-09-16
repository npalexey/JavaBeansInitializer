package com.nikitiuk.javabeansinitializer.collections;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlCollectedBeans {

    private Map<String, BeanMapper> beanCollectionsMap;
    private List<String> imports;
    private Map<String, String> mainMethodMap;

    public XmlCollectedBeans() {
    }

    public String toString() {

        if (imports != null || beanCollectionsMap != null || mainMethodMap != null) {
            return String.format("Imports: %s\nBean Collection List: %s\nMain Method: %s",
                    StringUtils.defaultIfBlank(Objects.toString(imports, null), "no imports."),
                    StringUtils.defaultIfBlank(Objects.toString(beanCollectionsMap, null), "no bean collection list."),
                    StringUtils.defaultIfBlank(Objects.toString(mainMethodMap, null), "no main method."));
        } else {
            return "No beans were collected";
        }
    }

    public Map<String, BeanMapper> getBeanCollectionsMap() {
        return beanCollectionsMap;
    }

    public void setBeanCollectionsMap(Map<String, BeanMapper> beanCollectionsMap) {
        this.beanCollectionsMap = beanCollectionsMap;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(ArrayList<String> imports) {
        this.imports = imports;
    }

    public Map<String, String> getMainMethodMap() {
        return mainMethodMap;
    }

    public void setMainMethodMap(Map<String, String> mainMethodMap) {
        this.mainMethodMap = mainMethodMap;
    }
}