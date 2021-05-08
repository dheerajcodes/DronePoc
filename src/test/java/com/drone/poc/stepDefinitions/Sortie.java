package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.SortieWebhookService;
import com.drone.poc.errors.SortieError;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class Sortie extends CucumberStepDefinition {
    private static final Logger logger = Logger.getLogger(Sortie.class.getCanonicalName());

    @And("^sortie details are updated successfully$")
    public void sortieDetailsUpdated() {
        try {
            logger.debug("Step Definition: sortie details are updated successfully");
            logger.debug("Getting context item " + ContextItem.SERVICE);
            SortieWebhookService service = (SortieWebhookService) getScenarioContext().getItem(ContextItem.SERVICE);
            String droneId = service.getUrlParameter(SortieWebhookService.URL_PARAMETER_DRONE_ID);
            String instructionId = service.getRequestParameter(SortieWebhookService.REQUEST_PARAMETER_INSTRUCTION_ID);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .body("status", equalTo(HttpStatus.NO_CONTENT.value()))
                    .and()
                    .body("data.drone_id", equalTo(droneId))
                    .and()
                    .body("data.instruction_id", equalTo(instructionId))
                    .and()
                    .body("data.status", equalTo("updated"));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    @And("^sortie details failed to update with error (.*)$")
    public void sortieDetailsFailedToUpdate(String sortieError) {
        try {
            logger.debug("Step Definition: sortie details failed to update with error " + sortieError);
            SortieError error = SortieError.valueOf(sortieError);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .body("errors.status", contains(HttpStatus.NOT_FOUND.value()))
                    .and()
                    .body("errors.code", contains(error.getCode()))
                    .and()
                    .body("errors.detail", contains(error.getDetail()));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }
}
