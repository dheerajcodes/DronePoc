package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.DroneDetailService;
import com.drone.poc.endpoints.ServiceEndpoint;
import com.drone.poc.errors.DroneError;
import io.cucumber.java.en.And;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;


public class Drone extends CucumberStepDefinition {
    private static final Logger logger = Logger.getLogger(Drone.class.getCanonicalName());

    @And("^response contains list of drones$")
    public void responseContainsDroneList() {
        try {
            logger.debug("Step Definition: response contains list of drones");
            logger.debug("Getting context item " + ContextItem.RESPONSE);
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
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    @And("^response contains drone details$")
    public void responseContainsDroneDetails() {
        try {
            logger.debug("Step Definition: response contains drone details");
            logger.debug("Getting context item " + ContextItem.SERVICE);
            DroneDetailService service = (DroneDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
            String droneId = service.getUrlParameter(DroneDetailService.URL_PARAMETER_DRONE_ID);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
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
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    @And("^response contains drone error (.*)$")
    public void responseContainsDroneError(String droneError) {
        try {
            logger.debug("Step Definition: response contains drone error " + droneError);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            DroneError error = DroneError.valueOf(droneError);
            String errorDetail = error.getDetail();
            if (service instanceof DroneDetailService) {
                errorDetail =
                        !error.isDetailHasDroneId() ?
                                errorDetail :
                                String.format(errorDetail, service.getUrlParameter(DroneDetailService.URL_PARAMETER_DRONE_ID));
            }
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .body("errors.status", contains(error.getStatus()))
                    .and()
                    .body("errors.code", contains(error.getCode()))
                    .and()
                    .body("errors.detail", contains(errorDetail));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }
}
