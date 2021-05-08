package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ContextItem;
import com.drone.poc.endpoints.InstructionDetailService;
import com.drone.poc.endpoints.NewInstructionService;
import com.drone.poc.errors.InstructionError;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class Instruction extends CucumberStepDefinition {
    private static final Logger logger = Logger.getLogger(Instruction.class.getCanonicalName());

    @And("^new instruction is created successfully$")
    public void newInstructionCreated() {
        try {
            logger.debug("Step Definition: new instruction is created successfully");
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            logger.debug("Getting context item " + ContextItem.SERVICE);
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
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    @And("^response contains instruction details$")
    public void responseContainsInstructionDetails() {
        try {
            logger.debug("Step Definition: response contains instruction details");
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            InstructionDetailService service = (InstructionDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
            String instructionId = service.getUrlParameter(InstructionDetailService.URL_PARAMETER_INSTRUCTION_ID);
            response.then()
                    .assertThat()
                    .body("data.id", equalTo(instructionId))
                    .and()
                    .body("data", hasKey("status"));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    @And("^instruction is failed with error (.*)$")
    public void instructionFailedWithError(String instructionError) {
        try {
            logger.debug("Step Definition: instruction is failed with error " + instructionError);
            InstructionError error = InstructionError.valueOf(instructionError);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .body("status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value()))
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

    @And("^response does not contain instruction details$")
    public void responseDoesNotContainInstructionDetails() {
        try {
            logger.debug("Step Definition: response does not contain instruction details");
            logger.debug("Getting context item " + ContextItem.SERVICE);
            InstructionDetailService service = (InstructionDetailService) getScenarioContext().getItem(ContextItem.SERVICE);
            String instructionId = service.getUrlParameter(InstructionDetailService.URL_PARAMETER_INSTRUCTION_ID);
            String errorDetail = String.format("Instruction with id=%s not found", instructionId);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .body("errors.status", contains(HttpStatus.NOT_FOUND.value()))
                    .and()
                    .body("errors.code", contains("not_found"))
                    .and()
                    .body("errors.detail", contains(errorDetail));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }
}
