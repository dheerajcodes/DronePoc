package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.DroneDetailService;
import com.drone.poc.endpoints.ServiceEndpoint;
import com.drone.poc.errors.DroneError;
import io.cucumber.java.en.And;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;


public class Drone extends CucumberStepDefinition {
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

    @And("^response contains drone details$")
    public void responseContainsDroneDetails() {
        DroneDetailService service = (DroneDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
        String droneId = service.getUrlParameter(DroneDetailService.URL_PARAMETER_DRONE_ID);
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("data.id", equalTo(droneId))
                .and()
                .body("data", allOf(
                        hasKey("status"),
                        hasKey("charge_level"),
                        hasKey("model")
                ));
    }

    @And("^response contains drone error (.*)$")
    public void responseContainsDroneError(String droneError) {
        ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
        DroneError error = DroneError.valueOf(droneError);
        String errorDetail = error.getDetail();
        if (service instanceof DroneDetailService) {
            errorDetail =
                    !error.isDetailHasDroneId() ?
                            errorDetail :
                            String.format(errorDetail, service.getUrlParameter(DroneDetailService.URL_PARAMETER_DRONE_ID));
        }
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("errors.status", contains(error.getStatus()))
                .and()
                .body("errors.code", contains(error.getCode()))
                .and()
                .body("errors.detail", contains(errorDetail));

    }

}
