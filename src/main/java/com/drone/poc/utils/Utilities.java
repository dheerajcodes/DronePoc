package com.drone.poc.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Pattern;

public class Utilities {
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^\\d+(.\\d+),\\d+(.\\d+)$");

    public static <T> String objectToJson(T object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
        objectMapper.writeValue(jsonGenerator, object);
        return stringWriter.toString();
    }

    /**
     * Checks whether the given location coordinates has valid format.
     *
     * @param coordinates location in the format {@literal "lat,long"}
     * @return {@literal true} if given coordinates has valid format otherwise {@literal false}
     */
    public static boolean validateLocationCoordinates(String coordinates) {
        return LOCATION_PATTERN.matcher(coordinates).matches();
    }

    public static String removeWhitespaces(String source) {
        return source.replaceAll("\\s+", "");
    }
}
