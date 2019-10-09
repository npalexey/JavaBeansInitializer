package com.nikitiuk.javabeansinitializer.server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitiuk.javabeansinitializer.annotations.ApplicationCustomContext;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Consumes;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.FormDataParam;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.Path;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller.PathParam;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.helpers.Order;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Context;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.security.Filter;
import com.nikitiuk.javabeansinitializer.exceptions.RequestedParametersDoNotMatchTheMethodException;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import com.nikitiuk.javabeansinitializer.server.request.types.RequestContext;
import com.nikitiuk.javabeansinitializer.server.utils.MimeType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class MethodCaller {

    private static final Logger logger = LoggerFactory.getLogger(MethodCaller.class);
    private ApplicationCustomContext applicationCustomContext;
    private RequestContext requestContext;

    public Object callRequestedMethod(Request request) throws Exception {
        applicationCustomContext = ApplicationCustomContext.getApplicationCustomContext();
        requestContext = new RequestContext();
        Method method = MethodFinder.findRequestedMethod(request.getHttpMethod(), request.getUrl());
        if (checkForAuthHeader(request.getHeaders())) {
            requestContext.setSecurityInfo(request.getHeaders().get("Authorization"));
        } else {
            requestContext.setSecurityInfo("");
        }
        doAuthManagement();
        requestContext.setMethod(method);
        request.setRequestContext(requestContext);
        if(request.getRequestContext().getAbortResponse() != null) {
            return null;
        }
        if(method.getParameterCount() != 0) {
            if(countMethodParameters(method) != countRequestParameters(request)) {
                throw new RequestedParametersDoNotMatchTheMethodException("Parameters in request do not match the parameters of the method.");
            }
            Object[] parametersArgs = constructParametersToInvokeMethodWith(method, request);
            return method.invoke(applicationCustomContext.getControllerContainer().get(method.getDeclaringClass()), parametersArgs);
        } else {
            return method.invoke(applicationCustomContext.getControllerContainer().get(method.getDeclaringClass()));
        }
    }

    private boolean checkForAuthHeader(Map<String, String> headers) {
        return headers.containsKey("Authorization");
    }

    private void doAuthManagement() throws IllegalAccessException, InvocationTargetException {
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

    private Object[] constructParametersToInvokeMethodWith(Method method, Request request) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Parameter[] declaredParameters = method.getParameters();
        List<Object> paramList = new ArrayList<>();
        for(Parameter parameter : declaredParameters) {
            if (parameter.isAnnotationPresent(PathParam.class)) {
                String methodPath = method.getAnnotation(Path.class).value();
                String pathParam = "{" + parameter.getAnnotation(PathParam.class).value() + "}";
                int startIndexOfParam = StringUtils.indexOf(methodPath, pathParam);
                String param = StringUtils.substring(request.getUrl(), startIndexOfParam, StringUtils.indexOf(request.getUrl(), "/", startIndexOfParam + 1));
                paramList.add(createObjectOfCertainTypeFromParamStringValue(param, parameter));
            } else if (parameter.isAnnotationPresent(FormDataParam.class)) {
                String formDataParamPartName = parameter.getAnnotation(FormDataParam.class).value();
                if(parameter.getType() == InputStream.class) {
                    formDataParamPartName = "file:" + formDataParamPartName;
                    logger.info("Class of parameter: InputStream.");
                    paramList.add(new ByteArrayInputStream(request.getBody().get(formDataParamPartName)));
                } else {
                    String param = new String(request.getBody().get(formDataParamPartName)).trim();
                    paramList.add(createObjectOfCertainTypeFromParamStringValue(param, parameter));
                }
            } else if(parameter.isAnnotationPresent(Context.class)) {
                paramList.add(request.getRequestContext());
            } else if(method.isAnnotationPresent(Consumes.class) &&
                    Arrays.asList(method.getAnnotation(Consumes.class).value()).contains(MimeType.APPLICATION_JSON) &&
                    request.getHeaders().containsValue(MimeType.APPLICATION_JSON)){
                ObjectMapper objectMapper = new ObjectMapper();
                paramList.add(objectMapper.readValue(new String(request.getBody().get("JsonArray")), parameter.getType()));
            } else {
                throw new IllegalAccessException("Request is not formatted correctly to access this method.");
            }
        }
        return paramList.toArray();
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
            invokeOnlyFilterMethods(entry.getValue().getDeclaredMethods(), securityMap.get(entry.getValue()));
            if(requestContext.getAbortResponse() != null) {
                break;
            }
        }
    }

    private void invokeFilterForSingleSecurityProvider(Map<Class, Object> securityMap) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Class, Object> entry : securityMap.entrySet()) {
            invokeOnlyFilterMethods(entry.getKey().getDeclaredMethods(), entry.getValue());
            if(requestContext.getAbortResponse() != null) {
                break;
            }
        }
    }

    private void invokeOnlyFilterMethods(Method[] declaredMethods, Object classWhoseMethodToInvoke) throws InvocationTargetException, IllegalAccessException {
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Filter.class)) {
                method.invoke(classWhoseMethodToInvoke);
            }
        }
    }

    private int countMethodParameters(Method method) {
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : paramAnnotations) {
            for (Annotation an : annotations) {
                if(an.annotationType().equals(Context.class)) {
                    return method.getParameterCount() - 1;
                }
            }
        }
        return method.getParameterCount();
    }

    private int countRequestParameters(Request request) {
        return request.getBody().size() + countUrlParameters(request.getUrl());
    }

    private int countUrlParameters(String url) {
        return StringUtils.countMatches(url,"{");
    }

    private Object createObjectOfCertainTypeFromParamStringValue(String param, Parameter parameter) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> classOfParameter = parameter.getType();
        if(classOfParameter.isPrimitive()) {
            if(classOfParameter == Long.class || classOfParameter == Long.TYPE) return Long.parseLong(param);
            if(classOfParameter == Integer.class || classOfParameter == Integer.TYPE) return Integer.parseInt(param);
            if(classOfParameter == Double.class || classOfParameter == Double.TYPE) return Double.parseDouble(param);
            if(classOfParameter == Boolean.class || classOfParameter == Boolean.TYPE) return Boolean.parseBoolean(param);
            if(classOfParameter == Byte.class || classOfParameter == Byte.TYPE) return Byte.parseByte(param);
            if(classOfParameter == Short.class || classOfParameter == Short.TYPE) return Short.parseShort(param);
            if(classOfParameter == Float.class || classOfParameter == Float.TYPE) return Float.parseFloat(param);
        }
        Constructor<?> cons = classOfParameter.getConstructor(String.class);
        logger.info(String.format("Class of parameter: %s; its value: %s.", classOfParameter.getSimpleName(), param));
        return cons.newInstance(param);
    }
}