package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.Controller;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@Path("/docs")
public class ControllerBean {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBean.class);

    @AutoWire
    private TestBeanDependent someServiceBean;

    @GET
    @Path("/age")
    public int getAgeOfServiceBean() {
        int age = someServiceBean.getAutoWiredBean().getSomeAge();
        logger.info(Integer.toString(age));
        return age;
    }

    public double callCountOfServiceBean() {
        return someServiceBean.count();
    }
}