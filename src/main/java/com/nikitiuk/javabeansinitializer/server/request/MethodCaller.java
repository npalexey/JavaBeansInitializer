package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.exceptions.MethodNotFoundException;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodCaller {

    private ApplicationCustomContext applicationCustomContext;

    public void callRequestedMethod(Request request) throws MethodNotFoundException {
        applicationCustomContext = ApplicationCustomContext.getApplicationCustomContext();
        RequestContext requestContext = new RequestContext();
        Method method = findRequestedMethod(request.getHttpMethod(), request.getUrl());
        if (checkForAuthHeader(request.getHeaders())) {
            requestContext.setSecurityData(request.getHeaders().get("Authorization"));
        }
    }

    private Method findRequestedMethod(RequestMethod httpMethod, String url) throws MethodNotFoundException {
        for(Map.Entry<Class, Object> entry : applicationCustomContext.getControllerContainer().entrySet()) {
            Class beanClass = entry.getKey();
            Class<?> beanClassInstance = (Class<?>) entry.getValue();
            if(beanClassInstance.isAnnotationPresent(Path.class)) {
                String urlControllerPath, urlMethodPath;
                if(StringUtils.countMatches(url, "/") > 1) {
                    urlControllerPath = StringUtils.substring(url, 0, StringUtils.indexOf(url,"/",1))/*url.substring(0, url.indexOf("/", 1))*/;
                    urlMethodPath = StringUtils.substring(url, StringUtils.indexOf(url,"/",1))/*url.substring(url.indexOf("/", 1))*/;
                } else {
                    urlControllerPath = url;
                    urlMethodPath = null;
                }
                String controllerPath = beanClassInstance.getAnnotation(Path.class).value();
                if(StringUtils.equalsIgnoreCase(controllerPath, urlControllerPath)/*.equals(urlControllerPath)*/) {
                    for (Method method : beanClass.getDeclaredMethods()) {
                        method.setAccessible(true);
                        ifCertainHttp:
                            if (method.isAnnotationPresent(httpMethod.getAnnotationClassOfHttpMethod())) {
                                if(method.isAnnotationPresent(Path.class)) {
                                    String methodPath = method.getAnnotation(Path.class).value();
                                    if(urlMethodPath == null && StringUtils.equals(methodPath, "/")) {
                                        return method;
                                    }
                                    if(urlMethodPath == null) {
                                        break ifCertainHttp;
                                    }
                                    int slashesInMethodPath = StringUtils.countMatches(methodPath, "/");
                                    if (!StringUtils.contains(methodPath, "{")) {
                                        if(StringUtils.equalsIgnoreCase(methodPath, urlMethodPath)) {
                                            return method;
                                        }
                                    } else if(slashesInMethodPath > 1 && StringUtils.endsWith(urlMethodPath, "}")) {
                                        String cutUrlMethodPath = StringUtils.substring(urlMethodPath, 0, StringUtils.lastIndexOf(urlMethodPath, "/"));
                                        String cutMethodPath = StringUtils.substring(methodPath, 0, StringUtils.lastIndexOf(methodPath, "/"));
                                        if(StringUtils.equalsIgnoreCase(cutMethodPath, cutUrlMethodPath)) {
                                            return method;
                                        }
                                    } else if(slashesInMethodPath <= 1) {
                                        return method;
                                    } else {
                                        String urlMethodPathWithParameterCutOff = StringUtils.substringBefore(urlMethodPath,"{") + StringUtils.substringAfter(urlMethodPath,"}");
                                        String methodPathWithParameterCutOff = StringUtils.substringBefore(methodPath,"{") + StringUtils.substringAfter(methodPath,"}");
                                        if(StringUtils.equalsIgnoreCase(methodPathWithParameterCutOff, urlMethodPathWithParameterCutOff)) {
                                            return method;
                                        }
                                    }
                                } else if(StringUtils.isBlank(urlMethodPath) || StringUtils.equals(urlMethodPath, "/")) {
                                    return method;
                                }
                            }
                    }
                }
            }
        }
        throw new MethodNotFoundException("Provided url doesn't state a correct method to invoke.");
        /*applicationCustomContext.getControllerContainer().forEach((beanClass, beanClassInstance) -> {
            Class<?> beanClassInstanceGen = (Class<?>) beanClassInstance;
            if(beanClassInstanceGen.isAnnotationPresent(Path.class)) {
                String urlControllerPath = url.substring(0, url.indexOf("/", 1));
                String controllerPath = beanClassInstanceGen.getAnnotation(Path.class).value();
                if(controllerPath.equals(urlControllerPath)) {
                    for (Method method : beanClass.getDeclaredMethods()) {
                        method.setAccessible(true);
                        if (method.isAnnotationPresent(httpMethod.getAnnotationClassOfHttpMethod()) && method.isAnnotationPresent(Path.class)) {
                            String urlMethodPath = url.substring(url.indexOf("/", 1));
                            String methodPath = method.getAnnotation(Path.class).value();
                            int slashesInMethodPath = StringUtils.countMatches(methodPath, "/");
                            if (!StringUtils.contains(methodPath, "{")) {
                                if(StringUtils.equals(urlMethodPath, methodPath)) {
                                    returnMethod = method;
                                }
                            }
                        }
                    }
                }
            }
        });*/
    }

    private boolean checkForAuthHeader(Map<String, String> headers) {
        return headers.containsKey("Authorization");
    }
}