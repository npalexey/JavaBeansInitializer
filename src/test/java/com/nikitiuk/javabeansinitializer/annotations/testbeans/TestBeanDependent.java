package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.Bean;
import com.nikitiuk.javabeansinitializer.annotations.Value;

@Bean
public class TestBeanDependent {

    @Value("254.35")
    private Double someDouble;

    @AutoWire
    private TestBeanIndependent autoWiredBean;

    public Double getSomeDouble() {
        return someDouble;
    }

    public void setSomeDouble(Double someDouble) {
        this.someDouble = someDouble;
    }

    public TestBeanIndependent getAutoWiredBean() {
        return autoWiredBean;
    }

    public void setAutoWiredBean(TestBeanIndependent autoWiredBean) {
        this.autoWiredBean = autoWiredBean;
    }

    public double count() {

        return getSomeDouble() + autoWiredBean.getSomeAge();
    }
}
