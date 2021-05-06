package com.drone.poc.setup;

import io.cucumber.java.Scenario;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ScenarioInitializer {
    private static final String PATTERN_TEST_TAG = "^T_\\d{3}$";

    public static void setupPreconditions(Scenario scenario, Environment environment) {
        ArrayList<String> testTags =
                scenario.getSourceTagNames()
                        .stream()
                        .filter((tag) -> tag.matches(PATTERN_TEST_TAG))
                        .collect(Collectors.toCollection(ArrayList::new));
        // TODO: Throw error when testTags are empty
        String testTag = testTags.get(0);
        switch (testTag) {
            case "T_001":
                // Create at least one drone in the database
                break;
            case "T_002":
                // Create drone with id T_002_DRONE_ID in the database
                break;
            case "T_003":
                // Create drone with id T_003_DRONE_ID in the database and drone is not busy
                break;
            case "T_004":
                // Create instruction with id T_004_INSTRUCTION_ID in the database
                break;
            case "T_005":
                // Create drone with id T_005_DRONE_ID and instruction with id T_005_INSTRUCTION_ID in the database
                // And also create a sortie record for given drone and instruction
                break;
            case "T_006":
                // Delete all drones from the database
                break;
            case "T_007":
                // Make sure drone with id T_007_DRONE_ID is not present in the database
                break;
            case "T_008":
            case "T_009":
                // Make sure drone with id T_008_DRONE_ID is not present in the database
                break;
            case "T_010":
            case "T_011":
                // Create drones with ids T_010_DRONE_ID and T_011_DRONE_ID in the database
                // Also drones are not busy
                break;
            case "T_012":
            case "T_013":
                // Create drones with ids T_012_DRONE_ID and T_013_DRONE_ID in the database
                // Also drones are not busy
                break;
            case "T_014":
            case "T_015":
                // Create drones with ids T_014_DRONE_ID and T_015_DRONE_ID in the database
                // Also drones are not busy
                break;
            case "T_016":
                // Create drone with id T_016_DRONE_ID in the database and drone is busy
                break;
            case "T_017":
                // Make sure instruction with id T_017_INSTRUCTION_ID is not present in the database
                break;
            case "T_018":
                // Make sure drone with id T_018_DRONE_ID is not present in the database
                // Create instruction with id T_018_INSTRUCTION_ID in the database
                break;
            case "T_019":
                // Create drone with id T_019_DRONE_ID in the database
                // Make sure instruction with id T_019_INSTRUCTION_ID is not present in the database
                break;
        }
    }
}
