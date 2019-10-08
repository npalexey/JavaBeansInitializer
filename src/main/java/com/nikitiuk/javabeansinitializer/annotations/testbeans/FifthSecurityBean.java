package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Context;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Filter;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Provider;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class FifthSecurityBean {

    private static final Logger logger = LoggerFactory.getLogger(SecurityBean.class);

    @Context
    private RequestContext requestContext;

    @Filter
    public void filter() {
        logger.info(requestContext.getSecurityInfo() + " And this from fifth.");
    }
}

