package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.DELETE;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.GET;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.POST;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.http.PUT;

import java.lang.annotation.Annotation;

public enum RequestMethod {
    GET{
        @Override
        public Class<? extends Annotation> getAnnotationClassOfHttpMethod() {
            return GET.class;
        }
    },
    POST{
        @Override
        public Class<? extends Annotation> getAnnotationClassOfHttpMethod() {
            return POST.class;
        }
    },
    PUT{
        @Override
        public Class<? extends Annotation> getAnnotationClassOfHttpMethod() {
            return PUT.class;
        }
    },
    DELETE{
        @Override
        public Class<? extends Annotation> getAnnotationClassOfHttpMethod() {
            return DELETE.class;
        }
    };

    public Class<? extends Annotation> getAnnotationClassOfHttpMethod() {
        return null;
    }
}
