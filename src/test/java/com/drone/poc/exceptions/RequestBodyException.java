package com.drone.poc.exceptions;

public class RequestBodyException extends RuntimeException {
    public RequestBodyException(String serviceName, String message) {
        super(serviceName + ": " + message);
    }
}
