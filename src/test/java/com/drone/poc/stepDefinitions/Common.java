package com.drone.poc.stepDefinitions;


import com.drone.poc.context.ContextItem;

import com.drone.poc.endpoints.*;
import com.drone.poc.exceptions.UnknownServiceEndpoint;
import com.drone.poc.setup.TestScenarioManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


public class Common extends CucumberStepDefinition {

    @Autowired
    private TestScenarioManager scenarioManager;

    private static final Logger logger = Logger.getLogger(Common.class.getCanonicalName());

    @Before
    public void beforeScenario(Scenario scenario) {
        try {
            logger.debug("Before scenario: " + scenario.getName());
            logger.debug("Setting up scenario preconditions ...");
            scenarioManager.setupPreconditions(scenario);
            logger.debug("Scenario preconditions setup completed");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            logger.debug("After scenario: " + scenario.getName());
            logger.debug("Undoing scenario preconditions ...");
            scenarioManager.undoPreconditions(scenario);
            logger.debug("Scenario preconditions undo completed");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Create service object corresponds to given service name and save it in the scenario context.
     *
     * @param serviceName Service Name
     */
    @Given("^endpoint is (.*)$")
    public void endPointIs(String serviceName) {
        try {
            logger.debug("Step Definition: endpoint is " + serviceName);
            String baseUrl = getTestData("BASE_URL");
            String basePath = getTestData("BASE_PATH");
            if (baseUrl == null || baseUrl.trim().isEmpty())
                throw new RuntimeException("BASE_URL is not set in test.data.properties file");
            if (basePath == null || basePath.trim().isEmpty())
                throw new RuntimeException("BASE_PATH is not set in test.data.properties file");

            ServiceEndpoint service;
            switch (serviceName) {
                case "Drone List Service":
                    service = new DroneListService(baseUrl, basePath);
                    break;
                case "Drone Detail Service":
                    service = new DroneDetailService(baseUrl, basePath);
                    break;
                case "New Instruction Service":
                    service = new NewInstructionService(baseUrl, basePath);
                    break;
                case "Instruction Detail Service":
                    service = new InstructionDetailService(baseUrl, basePath);
                    break;
                case "Sortie Webhook Service":
                    service = new SortieWebhookService(baseUrl, basePath);
                    break;
                default:
                    throw new UnknownServiceEndpoint(serviceName);
            }
            logger.debug("Setting context item " + ContextItem.SERVICE);
            getScenarioContext().setItem(ContextItem.SERVICE, service);
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }


    /**
     * Add the endpoint url parameter in the service object.
     *
     * @param parameterName     url parameter name
     * @param parameterValueKey key to obtain url parameter value from test data
     */
    @And("^endpoint url parameter (.*) is (.*)$")
    public void endpointUrlParameterIs(String parameterName, String parameterValueKey) {
        try {
            logger.debug("Step Definition: endpoint url parameter " + parameterName + " is " + parameterValueKey);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            String parameterValue = getTestData(parameterValueKey);
            service.addUrlParameter(parameterName, parameterValue);
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Set the http request method in the service object.
     *
     * @param requestMethod Http request method (GET,POST,PUT,DELETE,etc.)
     */
    @And("^request method is (.*)$")
    public void requestMethodIs(String requestMethod) {
        try {
            logger.debug("Step Definition: request method is " + requestMethod);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            service.setRequestMethod(Method.valueOf(requestMethod));
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Set the content type for the accepts request header in the service object.
     *
     * @param contentType Content Type Value
     */
    @And("^request accepts (.*)$")
    public void requestAcceptsContentType(String contentType) {
        try {
            logger.debug("Step Definition: request accepts " + contentType);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            service.setRequestAccepts(ContentType.valueOf(contentType));
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Set the content type of the request body in the service object.
     *
     * @param contentType Content Type Value
     */
    @And("^request has (.*) content$")
    public void requestHasContentType(String contentType) {
        try {
            logger.debug("Step Definition: request has " + contentType + " content");
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            service.setRequestContentType(ContentType.valueOf(contentType));
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Set the request body parameter in the service object.
     *
     * @param parameterName     request parameter name
     * @param parameterValueKey key to obtain request parameter value from test data
     */
    @And("^request parameter (.*) is (.*)$")
    public void requestParameterIs(String parameterName, String parameterValueKey) {
        try {
            logger.debug("Step Definition: request parameter " + parameterName + " is " + parameterValueKey);
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            String parameterValue = getTestData(parameterValueKey);
            service.addRequestParameter(parameterName, parameterValue);
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Send the request using service object and save the response in scenario context.
     */
    @When("^request is sent$")
    public void requestIsSent() {
        try {
            logger.debug("Step Definition: request is sent");
            logger.debug("Getting context item " + ContextItem.SERVICE);
            ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
            Response response = service.sendRequest();
            logger.debug("Setting context item " + ContextItem.RESPONSE);
            getScenarioContext().setItem(ContextItem.RESPONSE, response);
            logger.debug("STEP OK");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }

    /**
     * Asserts that http status of service response matches the given http status.
     *
     * @param responseStatus Http Status Value
     */
    @Then("^response is received with status (.*)$")
    public void responseReceivedWithStatus(String responseStatus) {
        try {
            logger.debug("Step Definition: response is received with status " + responseStatus);
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.valueOf(responseStatus).value());
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }

    /**
     * Asserts that content type of service request body matches the given content type.
     *
     * @param contentType Content Type Value
     */
    @And("^response has (.*) content$")
    public void responseHasContentType(String contentType) {
        try {
            logger.debug("response has " + contentType + " content");
            logger.debug("Getting context item " + ContextItem.RESPONSE);
            Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
            response.then()
                    .assertThat()
                    .contentType(ContentType.valueOf(contentType));
            logger.debug("STEP OK");
        } catch (AssertionError | Exception errorException) {
            logger.error(errorException.getMessage());
            throw errorException;
        }
    }
}
