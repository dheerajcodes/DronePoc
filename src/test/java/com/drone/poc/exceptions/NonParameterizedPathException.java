package com.drone.poc.exceptions;

public class NonParameterizedPathException extends RuntimeException {
    public NonParameterizedPathException(String serviceName) {
        super(serviceName + ": Endpoint path does not contain any parameters.");
    }
}
