package com.nikitiuk.javabeansinitializer.collections;

import java.util.ArrayList;
import java.util.Map;

public class XmlCollectedBeans {
    private Map<String, BeanMapper> beanCollectionsMap;
    private ArrayList<String> imports;
    private Map<String, String> mainMethodMap;

    public XmlCollectedBeans(){
    }

    public String toString(){
        if (imports != null && beanCollectionsMap != null && mainMethodMap != null){
            return "Imports: " + imports.toString() + "\nBean Collection List: " + beanCollectionsMap.toString() + "\nMain method: " + mainMethodMap.toString();
        } else if(imports != null && beanCollectionsMap != null){
            return "Imports: " + imports.toString() + "\nBean Collection List: " + beanCollectionsMap.toString();
        } else if(beanCollectionsMap != null && mainMethodMap != null){
            return "Bean Collection List: " + beanCollectionsMap.toString() + "\nMain method: " + mainMethodMap.toString();
        } else if(beanCollectionsMap != null) {
            return "Bean Collection List: " + beanCollectionsMap.toString();
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

    public ArrayList<String> getImports() {
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
