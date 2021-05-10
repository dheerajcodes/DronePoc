package com.drone.poc.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * This class represents a service endpoint where requests can be sent.
 * Subclasses provide specific implementation regarding endpoint path, parameters. etc.
 */
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

    /**
     * Set the content type of response that service request supports.
     *
     * @param contentType Content Type
     */
    public void setRequestAccepts(ContentType contentType) {
        this.requestSpec.accept(contentType);
    }

    /**
     * Set the content type of request body
     *
     * @param contentType Content Type of request body
     */
    public void setRequestContentType(ContentType contentType) {
        this.requestSpec.contentType(contentType);
    }

    /**
     * Add a parameter to the service url
     *
     * @param key   Parameter Key
     * @param value Parameter Value
     */
    public void addUrlParameter(String key, String value) {
        this.urlParameters.put(key, value);
    }

    /**
     * Add a parameter to service request body
     *
     * @param key   Parameter Key
     * @param value Parameter Value
     */
    public void addRequestParameter(String key, String value) {
        this.requestParameters.put(key, value);
    }

    /**
     * Get the service url parameter
     *
     * @param key Parameter Key
     * @return parameter value or null if parameter key does not exist
     */
    public String getUrlParameter(String key) {
        return this.urlParameters.get(key);
    }

    /**
     * Get the service request parameter
     *
     * @param key Parameter Key
     * @return parameter value or null if parameter key does not exist
     */
    public String getRequestParameter(String key) {
        return this.requestParameters.get(key);
    }

    /**
     * Get the service path url where request is sent.
     * This method by default returns endpoint path.
     * Subclasses can override this method to define custom behaviour for obtaining
     * service path url.
     *
     * @return Service path url
     */
    protected String getRequestPath() {
        return this.getEndPointPath();
    }

    /**
     * Returns request body for the service request.
     * By default this method returns null to represent empty request body.
     * Subclasses can override this method to provide any request body to send along with service request.
     *
     * @return Request body content or null if request has no body
     */
    protected String getRequestBody() {
        return null;
    }


    /**
     * This method sends request to the service request path.
     *
     * @return Service response
     */
    public Response sendRequest() {
        String requestPath = this.getRequestPath(); // Get request path
        String requestBody = this.getRequestBody(); // Get request body
        // Set url parameters (if any)
        if (!urlParameters.isEmpty()) this.requestSpec.pathParams(urlParameters);
        // Set request body (if any)
        if (requestBody != null && !requestBody.trim().isEmpty()) this.requestSpec.body(requestBody);
        // send the request and return response
        return this.requestSpec.request(this.getRequestMethod(), requestPath);
    }
}
