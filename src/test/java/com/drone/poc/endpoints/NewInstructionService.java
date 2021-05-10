package com.drone.poc.endpoints;

import com.drone.poc.exceptions.NonParameterizedPathException;
import com.drone.poc.exceptions.RequestBodyException;
import com.drone.poc.exceptions.UnknownRequestParameterException;

public class NewInstructionService extends ServiceEndpoint {

    // Request parameters
    public static final String REQUEST_PARAMETER_DRONE_ID = "drone-id";
    public static final String REQUEST_PARAMETER_WAREHOUSE_LOCATION = "warehouse-location";
    public static final String REQUEST_PARAMETER_INSTRUCTION_TYPE = "instruction-type";
    public static final String REQUEST_PARAMETER_DESTINATION_LOCATION = "destination-location";

    // Service Endpoint Path
    private static final String END_POINT_PATH = "/drones/instructions";
    private static final String SIMPLE_CLASS_NAME = NewInstructionService.class.getSimpleName();

    // Default Instruction Type
    private static final String DEFAULT_INSTRUCTION_TYPE = "pickup";

    // Request Body Template (with request parameters as placeholders)
    private static final String TEMPLATE_REQUEST_BODY = "{\n" +
            "    \"data\":{\n" +
            "        \"payload\":[\n" +
            "            {\n" +
            "                \"drone_id\": \"{" + REQUEST_PARAMETER_DRONE_ID + "}\",\n" +
            "                \"warehouse_loc\": \"{" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "}\",\n" +
            "                \"type\": \"{" + REQUEST_PARAMETER_INSTRUCTION_TYPE + "}\",\n" +
            "                \"destination_loc\": \"{" + REQUEST_PARAMETER_DESTINATION_LOCATION + "}\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    public NewInstructionService(String baseUrl, String basePath) {
        super(baseUrl, basePath, END_POINT_PATH);
    }

    @Override
    public void addUrlParameter(String key, String value) {
        // Since service endpoint path does not support parameters, hence exception is thrown.
        throw new NonParameterizedPathException(SIMPLE_CLASS_NAME);
    }

    @Override
    public void addRequestParameter(String key, String value) {
        switch (key) {
            case REQUEST_PARAMETER_DRONE_ID:
            case REQUEST_PARAMETER_WAREHOUSE_LOCATION:
            case REQUEST_PARAMETER_INSTRUCTION_TYPE:
            case REQUEST_PARAMETER_DESTINATION_LOCATION:
                super.addRequestParameter(key, value);
                break;
            default:
                // Unknown request parameter
                throw new UnknownRequestParameterException(SIMPLE_CLASS_NAME, key);
        }
    }

    @Override
    protected String getRequestBody() {
        // Construct request body
        String droneId = getRequestParameter(REQUEST_PARAMETER_DRONE_ID);
        String warehouseLocation = getRequestParameter(REQUEST_PARAMETER_WAREHOUSE_LOCATION);
        String instructionType = getRequestParameter(REQUEST_PARAMETER_INSTRUCTION_TYPE);
        String destinationLocation = getRequestParameter(REQUEST_PARAMETER_DESTINATION_LOCATION);

        if (droneId == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_DRONE_ID + "'");
        if (warehouseLocation == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "'");
        if (destinationLocation == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_DESTINATION_LOCATION + "'");

        instructionType = instructionType != null ? instructionType : DEFAULT_INSTRUCTION_TYPE;

        return TEMPLATE_REQUEST_BODY
                .replace("{" + REQUEST_PARAMETER_DRONE_ID + "}", droneId)
                .replace("{" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "}", warehouseLocation)
                .replace("{" + REQUEST_PARAMETER_INSTRUCTION_TYPE + "}", instructionType)
                .replace("{" + REQUEST_PARAMETER_DESTINATION_LOCATION + "}", destinationLocation);
    }
}
