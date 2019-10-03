package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.Controller;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.GET;

@Controller
@Path("/docs")
public class ControllerBean {

    @AutoWire
    private TestBeanDependent someServiceBean;

    @GET
    @Path("/age")
    public int getAgeOfServiceBean() {
        return someServiceBean.getAutoWiredBean().getSomeAge();
    }

    public double callCountOfServiceBean() {
        return someServiceBean.count();
    }
}