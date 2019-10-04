package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.helpers.Order;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Context;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Filter;
import com.nikitiuk.javabeansinitializer.exceptions.MethodNotFoundException;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MethodCaller {

    private static final Logger logger = LoggerFactory.getLogger(MethodCaller.class);
    private ApplicationCustomContext applicationCustomContext;

    public void callRequestedMethod(Request request) throws MethodNotFoundException, InvocationTargetException, IllegalAccessException {
        applicationCustomContext = ApplicationCustomContext.getApplicationCustomContext();
        RequestContext requestContext = new RequestContext();
        Method method = findRequestedMethod(request.getHttpMethod(), request.getUrl());
        if (checkForAuthHeader(request.getHeaders())) {
            requestContext.setSecurityData(request.getHeaders().get("Authorization"));
        } else {
            requestContext.setSecurityData("");
        }
        doAuthManagement(requestContext);
        method.invoke(applicationCustomContext.getControllerContainer().get(method.getDeclaringClass()));
    }

    private Method findRequestedMethod(RequestMethod httpMethod, String url) throws MethodNotFoundException {
        for (Class<?> beanClass : applicationCustomContext.getControllerContainer().keySet()) {
            if (beanClass.isAnnotationPresent(Path.class)) {
                String urlControllerPath, urlMethodPath;
                if (StringUtils.countMatches(url, "/") > 1) {
                    urlControllerPath = StringUtils.substring(url, 0, StringUtils.indexOf(url, "/", 1))/*url.substring(0, url.indexOf("/", 1))*/;
                    urlMethodPath = StringUtils.substring(url, StringUtils.indexOf(url, "/", 1))/*url.substring(url.indexOf("/", 1))*/;
                } else {
                    urlControllerPath = url;
                    urlMethodPath = null;
                }
                String controllerPath = beanClass.getAnnotation(Path.class).value();
                if (StringUtils.equalsIgnoreCase(controllerPath, urlControllerPath)/*.equals(urlControllerPath)*/) {
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

    private boolean checkForAuthHeader(Map<String, String> headers) {
        return headers.containsKey("Authorization");
    }

    private void doAuthManagement(RequestContext requestContext) throws IllegalAccessException, InvocationTargetException {
        Map<Class, Object> securityMap = applicationCustomContext.getSecurityContainer();
        injectContext(securityMap, requestContext);
        invokeSecurityFilters(securityMap);
    }

    private Map<Integer, Class> sortByOrderAnnotation(Map<Class, Object> classObjectMap) {
        Map<Integer, Class> mapOfOrder = new HashMap<>();
        int maxCurrentOrder = Integer.MAX_VALUE;
        for (Map.Entry<Class, Object> entry : classObjectMap.entrySet()) {
            Class<?> entryClass = entry.getKey();
            if (entryClass.isAnnotationPresent(Order.class)) {
                int placement = entryClass.getAnnotation(Order.class).value();
                mapOfOrder.put(placement, entryClass);
            } else {
                mapOfOrder.put(maxCurrentOrder, entryClass);
                maxCurrentOrder -= 1;
            }
        }
        return mapOfOrder.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void injectContext(Map<Class, Object> classObjectMap, RequestContext requestContext) throws IllegalAccessException {
        for (Map.Entry<Class, Object> entry : classObjectMap.entrySet()) {
            for (Field field : entry.getKey().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Context.class)) {
                    field.set(entry.getValue(), requestContext);
                }
            }
        }
    }

    @Deprecated
    private void injectContextOld(Map<Class, Object> classObjectMap, RequestContext requestContext) throws IllegalAccessException {
        if (classObjectMap.size() == 1) {
            for (Map.Entry<Class, Object> entry : classObjectMap.entrySet()) {
                for (Field field : entry.getKey().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Context.class)) {
                        field.set(entry.getValue(), requestContext);
                    }
                }
            }
        } else if (classObjectMap.size() > 1) {
            Map<Integer, Class> mapOfOrder = new HashMap<>();
            int maxCurrentOrder = Integer.MAX_VALUE;
            for (Map.Entry<Class, Object> entry : classObjectMap.entrySet()) {
                Class<?> entryClass = entry.getKey();
                if (entryClass.isAnnotationPresent(Order.class)) {
                    int placement = entryClass.getAnnotation(Order.class).value();
                    mapOfOrder.put(placement, entryClass);
                } else {
                    mapOfOrder.put(maxCurrentOrder, entryClass);
                    maxCurrentOrder -= 1;
                }
            }
            Map<Integer, Class> sortedMap = mapOfOrder.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            logger.info("Sorted Map" + sortedMap.toString());
            for (Class<?> entryClass : sortedMap.values()) {
                for (Field field : entryClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Context.class)) {
                        field.set(classObjectMap.get(entryClass), requestContext);
                    }
                }
            }
        }

        /*anyBeanContainer.forEach((beanClass, beanClassInstance) -> {
            for (Field field : beanClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Value.class)) {
                    wireValue(field, beanClassInstance, field.getAnnotation(Value.class).value());
                } else if (field.isAnnotationPresent(AutoWire.class)) {
                    wireOtherBean(field, beanClassInstance, containerToWire.get(field.getType()));
                }
            }
        });*/
    }

    private void invokeSecurityFilters(Map<Class, Object> securityMap) throws InvocationTargetException, IllegalAccessException {
        if(securityMap.size() > 1) {
            invokeFiltersForMultipleSecurityProviders(securityMap);
        } else {
            invokeFilterForSingleSecurityProvider(securityMap);
        }
    }

    private void invokeFiltersForMultipleSecurityProviders(Map<Class, Object> securityMap) throws InvocationTargetException, IllegalAccessException {
        Map<Integer, Class> sortedMap = sortByOrderAnnotation(securityMap);
        logger.info("Sorted Map: " + sortedMap.toString());
        for (Map.Entry<Integer, Class> entry : sortedMap.entrySet()) {
            /*for (Method method : entry.getValue().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Filter.class)) {
                    method.invoke(securityMap.get(entry.getValue()));
                }
            }*/
            invokeOnlyFilterMethods(entry.getValue().getDeclaredMethods(), securityMap.get(entry.getValue()));
        }
    }

    private void invokeFilterForSingleSecurityProvider(Map<Class, Object> securityMap) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Class, Object> entry : securityMap.entrySet()) {
            /*for (Method method : entry.getKey().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Filter.class)) {
                    method.invoke(entry.getValue());
                }
            }*/
            invokeOnlyFilterMethods(entry.getKey().getDeclaredMethods(), entry.getValue());
        }
    }

    private void invokeOnlyFilterMethods(Method[] declaredMethods, Object classWhoseMethodToInvoke) throws InvocationTargetException, IllegalAccessException {
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Filter.class)) {
                method.invoke(classWhoseMethodToInvoke);
            }
        }
    }
}