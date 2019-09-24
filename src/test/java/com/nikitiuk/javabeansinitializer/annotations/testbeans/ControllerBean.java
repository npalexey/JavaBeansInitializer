package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.Controller;

@Controller
public class ControllerBean {

    @AutoWire
    private TestBeanDependent someServiceBean;

    public int getAgeOfServiceBean() {
        return someServiceBean.getAutoWiredBean().getSomeAge();
    }

    public double callCountOfServiceBean() {
        return someServiceBean.count();
    }
}