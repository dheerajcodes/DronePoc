package com.drone.poc.exceptions;

public class TestIdTagNotFoundException extends RuntimeException {
    public TestIdTagNotFoundException(String scenarioName, int scenarioLine) {
        super("No test ID tag found for scenario : " + scenarioName + " : at line " + scenarioLine);
    }
}
