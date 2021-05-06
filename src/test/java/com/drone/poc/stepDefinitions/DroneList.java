package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import io.cucumber.java.en.And;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;


public class DroneList extends CucumberStepDefinition {
    @And("^response contains list of drones$")
    public void responseContainsDroneList() {
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("status", equalTo(HttpStatus.OK.value()))
                .and()
                .body("data", not(emptyArray()))
                .and()
                .body("data", everyItem(
                        allOf(
                                hasKey("id"),
                                hasKey("status"),
                                hasKey("charge_level"),
                                hasKey("model")
                        )
                ));
    }
}
