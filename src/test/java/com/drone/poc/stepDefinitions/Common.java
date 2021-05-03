package com.drone.poc.stepDefinitions;


import com.drone.poc.context.ScenarioContext;

import org.springframework.beans.factory.annotation.Autowired;

public class Common extends CucumberStepDefinition {
    @Autowired
    private ScenarioContext context;
}
