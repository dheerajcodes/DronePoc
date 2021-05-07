package com.drone.poc.endpoints;

import com.drone.poc.exceptions.NoRequestContentSupportedException;
import com.drone.poc.exceptions.UnknownUrlParameterException;

public class InstructionDetailService extends ServiceEndpoint {
    public static final String URL_PARAMETER_INSTRUCTION_ID = "instruction-id";
    private static final String END_POINT_PATH = "/drones/instructions/{" + URL_PARAMETER_INSTRUCTION_ID + "}";
    private static final String SIMPLE_CLASS_NAME = InstructionDetailService.class.getSimpleName();

    public InstructionDetailService(String baseUrl, String basePath) {
        super(baseUrl, basePath, END_POINT_PATH);
    }

    @Override
    public void addUrlParameter(String key, String value) {
        switch (key) {
            case URL_PARAMETER_INSTRUCTION_ID:
                super.addUrlParameter(key, value);
                break;
            default:
                throw new UnknownUrlParameterException(SIMPLE_CLASS_NAME, key);
        }
    }

    @Override
    public void addRequestParameter(String key, String value) {
        throw new NoRequestContentSupportedException(SIMPLE_CLASS_NAME);
    }
}
