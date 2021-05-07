package com.drone.poc.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.drone.poc.stepDefinitions",
        plugin = {"pretty", "html:reports/cucumber-tests.html"}
)
public class TestRunner {
}
