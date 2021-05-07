package com.drone.poc.exceptions;

public class UnknownUrlParameterException extends RuntimeException {
    public UnknownUrlParameterException(String serviceName, String parameterName) {
        super(serviceName + " endpoint does not support url parameter '" + parameterName + "'");
    }
}
