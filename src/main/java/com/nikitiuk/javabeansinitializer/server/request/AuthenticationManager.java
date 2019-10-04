package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    private ApplicationCustomContext applicationCustomContext;

    public void doAuthManagement(RequestContext requestContext) {
        Map<Class, Object> securityMap = applicationCustomContext.getSecurityContainer();

        /*injectContext(securityMap, requestContext);
        invokeSecurityFilters(securityMap);*/
    }
}
