package com.nikitiuk.javabeansinitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeansCollection {
    private Map<String, Map<String, String>> beansMap;

    public BeansCollection(){
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map<String, Map<String, String>> beansMap) {
        this.beansMap = beansMap;
    }

    public List transformMapIntoMultipleBeans(Map<String, Map<String, String>> beansMap){
        List beansList = new ArrayList();
        return beansList;
    }
}
