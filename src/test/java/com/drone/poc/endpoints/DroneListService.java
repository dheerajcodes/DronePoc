package com.drone.poc.endpoints;

import com.drone.poc.exceptions.NoRequestContentSupportedException;
import com.drone.poc.exceptions.NonParameterizedPathException;

public class DroneListService extends ServiceEndpoint {
    private static final String END_POINT_PATH = "/drones";
    private static final String SIMPLE_CLASS_NAME = DroneListService.class.getSimpleName();

    public DroneListService(String baseUrl, String basePath) {
        super(baseUrl, basePath, END_POINT_PATH);
    }

    @Override
    public void addUrlParameter(String key, String value) {
        throw new NonParameterizedPathException(SIMPLE_CLASS_NAME);
    }

    @Override
    public void addRequestParameter(String key, String value) {
        throw new NoRequestContentSupportedException(SIMPLE_CLASS_NAME);
    }
}
