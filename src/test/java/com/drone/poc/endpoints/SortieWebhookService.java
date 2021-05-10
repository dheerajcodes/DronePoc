package com.drone.poc.endpoints;

import com.drone.poc.exceptions.RequestBodyException;
import com.drone.poc.exceptions.UnknownRequestParameterException;
import com.drone.poc.exceptions.UnknownUrlParameterException;

/**
 * This class represents SortieWebhookService endpoint.
 */
public class SortieWebhookService extends ServiceEndpoint {
    // URL parameter
    public static final String URL_PARAMETER_DRONE_ID = "drone-id";

    // Request parameters
    public static final String REQUEST_PARAMETER_INSTRUCTION_ID = "instruction-id";
    public static final String REQUEST_PARAMETER_CURRENT_LOCATION = "current-location";
    public static final String REQUEST_PARAMETER_DESTINATION_LOCATION = "destination-location";
    public static final String REQUEST_PARAMETER_WAREHOUSE_LOCATION = "warehouse-location";
    public static final String REQUEST_PARAMETER_STATUS = "status";
    public static final String REQUEST_PARAMETER_ETA = "estimated-time";
    public static final String REQUEST_PARAMETER_CURRENT_SPEED = "current-speed";

    // Service Endpoint Path
    private static final String END_POINT_PATH = "/drones/{" + URL_PARAMETER_DRONE_ID + "}/sorties";
    private static final String SIMPLE_CLASS_NAME = SortieWebhookService.class.getSimpleName();

    // Request Body Template (with request parameters as placeholders)
    private static final String TEMPLATE_REQUEST_BODY = "{\n" +
            "    \"data\":{\n" +
            "        \"payload\": [\n" +
            "            {\n" +
            "                \"instruction_id\": \"{" + REQUEST_PARAMETER_INSTRUCTION_ID + "}\",\n" +
            "                \"current_loc\": \"{" + REQUEST_PARAMETER_CURRENT_LOCATION + "}\",\n" +
            "                \"destination_loc\": \"{" + REQUEST_PARAMETER_DESTINATION_LOCATION + "}\",\n" +
            "                \"warehouse_loc\": \"{" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "}\",\n" +
            "                \"status\": \"{" + REQUEST_PARAMETER_STATUS + "}\",\n" +
            "                \"estimated_time_of_arrival_in_min\": \"{" + REQUEST_PARAMETER_ETA + "}\",\n" +
            "                \"current_speed_in_kmph\":\"{" + REQUEST_PARAMETER_CURRENT_SPEED + "}\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    public SortieWebhookService(String baseUrl, String basePath) {
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
        switch (key) {
            case REQUEST_PARAMETER_INSTRUCTION_ID:
            case REQUEST_PARAMETER_CURRENT_LOCATION:
            case REQUEST_PARAMETER_DESTINATION_LOCATION:
            case REQUEST_PARAMETER_WAREHOUSE_LOCATION:
            case REQUEST_PARAMETER_STATUS:
            case REQUEST_PARAMETER_ETA:
            case REQUEST_PARAMETER_CURRENT_SPEED:
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
        String instructionId = getRequestParameter(REQUEST_PARAMETER_INSTRUCTION_ID);
        String currentLocation = getRequestParameter(REQUEST_PARAMETER_CURRENT_LOCATION);
        String destinationLocation = getRequestParameter(REQUEST_PARAMETER_DESTINATION_LOCATION);
        String warehouseLocation = getRequestParameter(REQUEST_PARAMETER_WAREHOUSE_LOCATION);
        String status = getRequestParameter(REQUEST_PARAMETER_STATUS);
        String eta = getRequestParameter(REQUEST_PARAMETER_ETA);
        String currentSpeed = getRequestParameter(REQUEST_PARAMETER_CURRENT_SPEED);

        if (instructionId == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_INSTRUCTION_ID + "'");
        if (currentLocation == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_CURRENT_LOCATION + "'");
        if (destinationLocation == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_DESTINATION_LOCATION + "'");
        if (warehouseLocation == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "'");
        if (status == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_STATUS + "'");
        if (eta == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_ETA + "'");
        if (currentSpeed == null)
            throw new RequestBodyException(SIMPLE_CLASS_NAME, "Missing request body parameter '" + REQUEST_PARAMETER_CURRENT_SPEED + "'");

        return TEMPLATE_REQUEST_BODY
                .replace("{" + REQUEST_PARAMETER_INSTRUCTION_ID + "}", instructionId)
                .replace("{" + REQUEST_PARAMETER_CURRENT_LOCATION + "}", currentLocation)
                .replace("{" + REQUEST_PARAMETER_DESTINATION_LOCATION + "}", destinationLocation)
                .replace("{" + REQUEST_PARAMETER_WAREHOUSE_LOCATION + "}", warehouseLocation)
                .replace("{" + REQUEST_PARAMETER_STATUS + "}", status)
                .replace("{" + REQUEST_PARAMETER_ETA + "}", eta)
                .replace("{" + REQUEST_PARAMETER_CURRENT_SPEED + "}", currentSpeed);
    }
}
