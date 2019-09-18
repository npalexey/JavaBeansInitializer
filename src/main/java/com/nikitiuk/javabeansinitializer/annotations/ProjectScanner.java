package com.nikitiuk.javabeansinitializer.annotations;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.util.Set;

public class ProjectScanner {

    private Reflections reflections;

    public void setReflections(String packageToScan) {
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageToScan))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.test", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.webapp", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.resources", packageToScan)))
                .useParallelExecutor());
    }

    public Reflections getReflections() {
        return reflections;
    }

    public Set<Class<?>> getBeans() {
        return reflections.getTypesAnnotatedWith(Bean.class, true);
    }

    public Set<Class<?>> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class, true);
    }

    /*public Set<Field> getValues() {
        return reflections.getFieldsAnnotatedWith(Value.class);
    }

    public Set<Field> getBeansToAutoWire() {
        return reflections.getFieldsAnnotatedWith(AutoWire.class);
    }*/
}