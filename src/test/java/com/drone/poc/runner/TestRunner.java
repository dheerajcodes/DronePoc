package com.drone.poc.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.drone.poc.stepDefinitions",
        plugin = {"pretty", "html:reports/cucumber-tests.html"}
)
@PropertySource("classpath:test.properties")
public class TestRunner {
}
