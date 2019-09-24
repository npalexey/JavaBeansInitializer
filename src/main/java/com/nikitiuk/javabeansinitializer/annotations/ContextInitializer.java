package com.nikitiuk.javabeansinitializer.annotations;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.AutoWire;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ContextInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ContextInitializer.class);
    private ProjectScanner projectScanner = new ProjectScanner();

    public ApplicationCustomContext initializeContext(String packageName) {
        projectScanner.setReflections(packageName);
        ApplicationCustomContext applicationCustomContext = new ApplicationCustomContext();
        applicationCustomContext.setBeanContainer(createBeans());
        applicationCustomContext.setControllerContainer(createControllers());
        wireValuesAndOtherBeans(applicationCustomContext.getBeanContainer(), applicationCustomContext.getBeanContainer());
        wireValuesAndOtherBeans(applicationCustomContext.getControllerContainer(), applicationCustomContext.getBeanContainer());
        return applicationCustomContext;
    }

    private Map<Class, Object> createBeans() {
        Map<Class, Object> beanContainer = new HashMap<>();
        projectScanner.getBeans().forEach(beanClass -> {
            try {
                Constructor<?> constructor = beanClass.getConstructor();
                Object bean = constructor.newInstance();
                beanContainer.put(beanClass, bean);
            } catch (Exception e) {
                logger.error("Error while creating beans.", e);
            }
        });
        return beanContainer;
    }

    private Map<Class, Object> createControllers() {
        Map<Class, Object> controllerContainer = new HashMap<>();
        projectScanner.getControllers().forEach(controllerClass -> {
            try {
                Constructor<?> constructor = controllerClass.getConstructor();
                Object bean = constructor.newInstance();
                controllerContainer.put(controllerClass, bean);
            } catch (Exception e) {
                logger.error("Error while creating controllers.", e);
            }
        });
        return controllerContainer;
    }

    private void wireValuesAndOtherBeans(Map<Class, Object> anyBeanContainer, Map<Class, Object> containerToWire) {
       anyBeanContainer.forEach((beanClass, beanClassInstance) -> {
            for (Field field : beanClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Value.class)) {
                    wireValue(field, beanClassInstance, field.getAnnotation(Value.class).value());
                } else if (field.isAnnotationPresent(AutoWire.class)) {
                    wireOtherBean(field, beanClassInstance, containerToWire.get(field.getType()));
                }
            }
        });
    }

    private void wireValue(Field field, Object classWhoseFieldIsWired, String value) {
        try {
            Class<?> classOfField = field.getType();
            Constructor<?> cons = classOfField.getConstructor(String.class);
            logger.info(String.format("Class of field: %s; its value: %s; classWhoseFieldIsWired: %s.",
                    classOfField.getSimpleName(), value, classWhoseFieldIsWired.getClass().getSimpleName()));
            field.set(classWhoseFieldIsWired, cons.newInstance(value));
        } catch (Exception e) {
            logger.error("Error at ContextInitializer wireValue.", e);
        }
    }

    private void wireOtherBean(Field field, Object classWhoseFieldIsAnotherClass, Object classToWire) {
        try {
            logger.info(String.format("Wiring bean: %s, to class: %s.",
                    classToWire.getClass().getSimpleName(), classWhoseFieldIsAnotherClass.getClass().getSimpleName()));
            field.set(classWhoseFieldIsAnotherClass, classToWire);
        } catch (Exception e) {
            logger.error("Error at ContextInitializer wireOtherBean.", e);
        }
    }
}