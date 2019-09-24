package com.nikitiuk.javabeansinitializer.annotations;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.Bean;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Set;

public class ProjectScanner {

    private Reflections reflections;

    public void setReflections(String packageToScan) {
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageToScan))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.test", packageToScan)))
                /*.filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.webapp", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.resources", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.templates", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.appconfig.properties", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.log4j2.xml", packageToScan)))
                .filterInputsBy(new FilterBuilder().excludePackage(String.format("%s.tika-config.xml", packageToScan)))*/
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
}