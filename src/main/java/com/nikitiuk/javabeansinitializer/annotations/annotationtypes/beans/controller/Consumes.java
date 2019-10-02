package com.nikitiuk.javabeansinitializer.annotations.annotationtypes.beans.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD})
public @interface Consumes {

    String[] value() default "*/*";
}

