package com.drone.poc.exceptions;

public class UnknownServiceEndpoint extends RuntimeException {
    public UnknownServiceEndpoint(String serviceName) {
        super("Unknown Service Endpoint: " + serviceName);
    }
}
