package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.Bean;
import com.nikitiuk.javabeansinitializer.annotations.Value;

@Bean
public class TestBeanIndependent {

    @Value("SomeName")
    private String name;

    @Value("true")
    private Boolean booleanValue;

    @Value("35")
    private Integer someAge;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Integer getSomeAge() {
        return someAge;
    }

    public void setSomeAge(Integer someAge) {
        this.someAge = someAge;
    }
}
