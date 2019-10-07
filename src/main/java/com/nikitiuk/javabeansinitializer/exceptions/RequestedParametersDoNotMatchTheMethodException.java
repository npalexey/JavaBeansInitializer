package com.nikitiuk.javabeansinitializer.exceptions;

public class RequestedParametersDoNotMatchTheMethodException extends Exception {

    public RequestedParametersDoNotMatchTheMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestedParametersDoNotMatchTheMethodException(String message) {
        super(message);
    }

    public RequestedParametersDoNotMatchTheMethodException(Throwable cause) {
        super(cause);
    }
}
