package com.drone.poc.endpoints;

import com.drone.poc.exceptions.NoRequestContentSupportedException;
import com.drone.poc.exceptions.NonParameterizedPathException;

/**
 * This class represents Drone List Service endpoint.
 */
public class DroneListService extends ServiceEndpoint {
    // Service Endpoint Path
    private static final String END_POINT_PATH = "/drones";
    private static final String SIMPLE_CLASS_NAME = DroneListService.class.getSimpleName();

    public DroneListService(String baseUrl, String basePath) {
        super(baseUrl, basePath, END_POINT_PATH);
    }

    @Override
    public void addUrlParameter(String key, String value) {
        // Since service endpoint path does not support parameters, hence exception is thrown.
        throw new NonParameterizedPathException(SIMPLE_CLASS_NAME);
    }

    @Override
    public void addRequestParameter(String key, String value) {
        // Since service request does not support request body, hence exception is thrown.
        throw new NoRequestContentSupportedException(SIMPLE_CLASS_NAME);
    }
}
