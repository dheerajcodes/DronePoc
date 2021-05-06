package com.drone.poc.stepDefinitions;


import com.drone.poc.context.ContextItem;

import com.drone.poc.endpoints.DroneListService;
import com.drone.poc.endpoints.ServiceEndpoint;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class Common extends CucumberStepDefinition {

    @Before
    public void beforeScenario(Scenario scenario) {
    }

    @Given("^endpoint is (.*)$")
    public void endPointIs(String serviceName) {
        String baseUrl = super.getTestProperty("BASE_URL");
        String basePath = super.getTestProperty("BASE_PATH");
        if (baseUrl == null || baseUrl.trim().isEmpty())
            throw new RuntimeException("BASE_URL is not set in test.properties file");
        if (basePath == null || basePath.trim().isEmpty())
            throw new RuntimeException("BASE_PATH is not set in test.properties file");

        ServiceEndpoint service = null;
        switch (serviceName) {
            case "Drone List Service":
                service = new DroneListService(baseUrl, basePath);
        }
        getScenarioContext().setItem(ContextItem.SERVICE, service);
    }


    @And("^endpoint url parameter (.*) is (.*)$")
    public void endpointUrlParameterIs(String parameterName, String parameterValue) {
        ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
        service.addUrlParameter(parameterName, parameterValue);
    }

    @And("^request method is (.*)$")
    public void requestMethodIs(String requestMethod) {
        ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
        service.setRequestMethod(Method.valueOf(requestMethod));
    }

    @And("^request accepts (.*)$")
    public void requestAcceptsContentType(String contentType) {
        ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
        service.setRequestAccepts(ContentType.valueOf(contentType));
    }

    @When("^request is sent$")
    public void requestIsSent() {
        ServiceEndpoint service = (ServiceEndpoint) getScenarioContext().getItem(ContextItem.SERVICE);
        Response response = service.sendRequest();
        getScenarioContext().setItem(ContextItem.RESPONSE, response);
    }

    @Then("^response is received with status (.*)$")
    public void responseReceivedWithStatus(String responseStatus) {
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .statusCode(HttpStatus.valueOf(responseStatus).value());
    }

    @And("^response has (.*) content$")
    public void responseHasContentType(String contentType) {
        Response response = (Response) getScenarioContext().getItem(ContextItem.RESPONSE);
        response.then()
                .assertThat()
                .contentType(ContentType.valueOf(contentType));
    }
}
