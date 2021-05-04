package com.drone.poc.stepDefinitions;


import com.drone.poc.context.ScenarioContext;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Common extends CucumberStepDefinition {
    @Autowired
    private ScenarioContext context;

    @Before
    public void beforeScenario(Scenario scenario) {
    }
}
