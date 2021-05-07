package com.drone.poc.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public abstract class ServiceEndpoint {
    private final RequestSpecification requestSpec;
    @Getter
    private final String endPointPath;
    @Getter
    @Setter
    private Method requestMethod;
    private final HashMap<String, String> urlParameters;
    private final HashMap<String, String> requestParameters;

    public ServiceEndpoint(String baseUrl, String basePath, String endPointPath) {
        this.requestSpec = RestAssured.given();
        this.requestSpec.baseUri(baseUrl);
        this.requestSpec.basePath(basePath);
        this.endPointPath = endPointPath;
        this.urlParameters = new HashMap<>();
        this.requestParameters = new HashMap<>();
    }

    public void setRequestAccepts(ContentType contentType) {
        this.requestSpec.accept(contentType);
    }

    public void setRequestContentType(ContentType contentType) {
        this.requestSpec.contentType(contentType);
    }

    public void addUrlParameter(String key, String value) {
        this.urlParameters.put(key, value);
    }

    public void addRequestParameter(String key, String value) {
        this.requestParameters.put(key, value);
    }

    public String getUrlParameter(String key) {
        return this.urlParameters.get(key);
    }

    public String getRequestParameter(String key) {
        return this.requestParameters.get(key);
    }

    protected String getRequestPath() {
        return this.getEndPointPath();
    }

    protected abstract String getRequestBody();


    public Response sendRequest() {
        String requestPath = this.getRequestPath();
        String requestBody = this.getRequestBody();
        if (!urlParameters.isEmpty()) this.requestSpec.pathParams(urlParameters);
        if (requestBody != null && !requestBody.trim().isEmpty()) this.requestSpec.body(requestBody);
        return this.requestSpec.request(this.getRequestMethod(), requestPath);
    }
}
