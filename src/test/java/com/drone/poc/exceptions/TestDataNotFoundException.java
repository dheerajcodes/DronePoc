package com.drone.poc.exceptions;

public class TestDataNotFoundException extends RuntimeException {
    public TestDataNotFoundException(String key) {
        super("Test data not found for key " + key);
    }
}
