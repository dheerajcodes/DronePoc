package com.drone.poc.exceptions;

public class NoRequestContentSupportedException extends RuntimeException {
    public NoRequestContentSupportedException(String serviceName) {
        super(serviceName + ": Request does not support any content.");
    }
}
