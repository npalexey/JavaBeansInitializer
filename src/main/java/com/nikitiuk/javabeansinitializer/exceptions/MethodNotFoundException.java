package com.nikitiuk.javabeansinitializer.exceptions;

public class MethodNotFoundException extends Exception {

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
