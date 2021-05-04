package com.drone.poc.stepDefinitions;


import com.drone.poc.context.ScenarioContext;

import com.drone.poc.models.Drone;
import com.drone.poc.repositories.DroneRepository;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class Common extends CucumberStepDefinition {
    @Autowired
    private ScenarioContext context;
    @Autowired
    private DroneRepository droneRepository;

    @Given("^a drone with id (\\d+) is present$")
    public void droneWithIdIsPresent(long droneId) {
        Drone drone = new Drone();
    }

    @Given("^a drone with id (\\d+) is not present$")
    public void droneWithIdNotPresent(long droneId) {

    }
}
