package com.drone.poc.endpoints;

import com.drone.poc.exceptions.NoRequestContentSupportedException;
import com.drone.poc.exceptions.UnknownUrlParameterException;

/**
 * This class represents Drone Detail Service endpoint.
 */
public class DroneDetailService extends ServiceEndpoint {
    // URL Path Parameter
    public static final String URL_PARAMETER_DRONE_ID = "drone-id";
    // Service Endpoint Path
    private static final String END_POINT_PATH = "/drones/{" + URL_PARAMETER_DRONE_ID + "}";
    private static final String SIMPLE_CLASS_NAME = DroneListService.class.getSimpleName();

    public DroneDetailService(String baseUrl, String basePath) {
        super(baseUrl, basePath, END_POINT_PATH);
    }

    @Override
    public void addUrlParameter(String key, String value) {
        switch (key) {
            case URL_PARAMETER_DRONE_ID:
                super.addUrlParameter(key, value);
                break;
            default:
                // Unknown url parameter
                throw new UnknownUrlParameterException(SIMPLE_CLASS_NAME, key);
        }
    }

    @Override
    public void addRequestParameter(String key, String value) {
        // Since service request does not support request body, hence exception is thrown.
        throw new NoRequestContentSupportedException(SIMPLE_CLASS_NAME);
    }
}
