package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.SortieWebhookService;
import com.drone.poc.errors.SortieError;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class Sortie extends CucumberStepDefinition {
    @And("^sortie details are updated successfully$")
    public void sortieDetailsUpdated() {
        SortieWebhookService service = (SortieWebhookService) getScenarioContext().getItem(ContextItem.SERVICE);
        String droneId = service.getUrlParameter(SortieWebhookService.URL_PARAMETER_DRONE_ID);
        String instructionId = service.getRequestParameter(SortieWebhookService.REQUEST_PARAMETER_INSTRUCTION_ID);
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
    }

    @And("^sortie details failed to update with error (.*)$")
    public void sortieDetailsFailedToUpdate(String sortieError) {
        SortieError error = SortieError.valueOf(sortieError);
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("errors.status", contains(HttpStatus.NOT_FOUND.value()))
                .and()
                .body("errors.code", contains(error.getCode()))
                .and()
                .body("errors.detail", contains(error.getDetail()));
    }
}
