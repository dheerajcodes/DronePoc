package com.drone.poc.exceptions;

public class UnknownRequestParameterException extends RuntimeException {
    public UnknownRequestParameterException(String serviceName, String parameterName) {
        super(serviceName + " endpoint does not support request parameter '" + parameterName + "'");
    }
}
