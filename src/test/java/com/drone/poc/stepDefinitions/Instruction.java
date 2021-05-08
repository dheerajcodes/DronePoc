package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.InstructionDetailService;
import com.drone.poc.endpoints.NewInstructionService;
import com.drone.poc.errors.InstructionError;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class Instruction extends CucumberStepDefinition {
    @And("^new instruction is created successfully$")
    public void newInstructionCreated() {
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        NewInstructionService service = (NewInstructionService) getScenarioContext().getItem(ContextItem.SERVICE);
        String droneId = service.getRequestParameter(NewInstructionService.REQUEST_PARAMETER_DRONE_ID);
        response.then()
                .assertThat()
                .body("status", equalTo(HttpStatus.OK.value()))
                .and()
                .body("data.drone_id", equalTo(droneId))
                .and()
                .body("data", hasKey("instruction_id"))
                .and()
                .body("data.delivery_status", equalTo("in_progress"));
    }

    @And("^response contains instruction details$")
    public void responseContainsInstructionDetails() {
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        InstructionDetailService service = (InstructionDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
        String instructionId = service.getUrlParameter(InstructionDetailService.URL_PARAMETER_INSTRUCTION_ID);
        response.then()
                .assertThat()
                .body("data.id", equalTo(instructionId))
                .and()
                .body("data", hasKey("status"));
    }

    @And("^instruction is failed with error (.*)$")
    public void instructionFailedWithError(String instructionError) {
        InstructionError error = InstructionError.valueOf(instructionError);
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .and()
                .body("errors.code", contains(error.getCode()))
                .and()
                .body("errors.detail", contains(error.getDetail()));
    }

    @And("^response does not contain instruction details$")
    public void responseDoesNotContainInstructionDetails() {
        InstructionDetailService service = (InstructionDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
        String instructionId = service.getUrlParameter(InstructionDetailService.URL_PARAMETER_INSTRUCTION_ID);
        String errorDetail = String.format("Instruction with id=%s not found", instructionId);
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .body("errors.status", contains(HttpStatus.NOT_FOUND.value()))
                .and()
                .body("errors.code", contains("not_found"))
                .and()
                .body("errors.detail", contains(errorDetail));
    }
}
