package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.exceptions.MethodNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MethodFinder {

    private static final Logger logger = LoggerFactory.getLogger(MethodFinder.class);

    public static Method findRequestedMethod(RequestMethod httpMethod, String url) throws MethodNotFoundException {
        for (Class<?> beanClass : ApplicationCustomContext.getApplicationCustomContext().getControllerContainer().keySet()) {
            if (beanClass.isAnnotationPresent(Path.class)) {
                String urlControllerPath, urlMethodPath;
                if (StringUtils.countMatches(url, "/") > 1) {
                    urlControllerPath = StringUtils.substring(url, 0, StringUtils.indexOf(url, "/", 1));
                    urlMethodPath = StringUtils.substring(url, StringUtils.indexOf(url, "/", 1));
                } else {
                    urlControllerPath = url;
                    urlMethodPath = null;
                }
                String controllerPath = beanClass.getAnnotation(Path.class).value();
                if (StringUtils.equalsIgnoreCase(controllerPath, urlControllerPath)) {
                    for (Method method : beanClass.getDeclaredMethods()) {
                        method.setAccessible(true);
                        ifCertainHttp:
                        if (method.isAnnotationPresent(httpMethod.getAnnotationClassOfHttpMethod())) {
                            if (method.isAnnotationPresent(Path.class)) {
                                String methodPath = method.getAnnotation(Path.class).value();
                                if (urlMethodPath == null && StringUtils.equals(methodPath, "/")) {
                                    return method;
                                }
                                if (urlMethodPath == null) {
                                    break ifCertainHttp;
                                }
                                int slashesInMethodPath = StringUtils.countMatches(methodPath, "/");
                                if (!StringUtils.contains(methodPath, "{")) {
                                    if (StringUtils.equalsIgnoreCase(methodPath, urlMethodPath)) {
                                        return method;
                                    }
                                } else if (slashesInMethodPath > 1 && StringUtils.endsWith(urlMethodPath, "}")) {
                                    String cutUrlMethodPath = StringUtils.substring(urlMethodPath, 0, StringUtils.lastIndexOf(urlMethodPath, "/"));
                                    String cutMethodPath = StringUtils.substring(methodPath, 0, StringUtils.lastIndexOf(methodPath, "/"));
                                    if (StringUtils.equalsIgnoreCase(cutMethodPath, cutUrlMethodPath)) {
                                        return method;
                                    }
                                } else if (slashesInMethodPath <= 1) {
                                    return method;
                                } else {
                                    String urlMethodPathWithParameterCutOff = StringUtils.substringBefore(urlMethodPath, "{") + StringUtils.substringAfter(urlMethodPath, "}");
                                    String methodPathWithParameterCutOff = StringUtils.substringBefore(methodPath, "{") + StringUtils.substringAfter(methodPath, "}");
                                    if (StringUtils.equalsIgnoreCase(methodPathWithParameterCutOff, urlMethodPathWithParameterCutOff)) {
                                        return method;
                                    }
                                }
                            } else if (StringUtils.isBlank(urlMethodPath) || StringUtils.equals(urlMethodPath, "/")) {
                                return method;
                            }
                        }
                    }
                }
            }
        }
        throw new MethodNotFoundException("Provided url doesn't state a correct method to invoke.");
    }
}
