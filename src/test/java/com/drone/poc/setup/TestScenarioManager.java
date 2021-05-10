package com.drone.poc.setup;

import com.drone.poc.exceptions.TestDataNotFoundException;
import com.drone.poc.exceptions.TestIdTagNotFoundException;
import com.drone.poc.models.Drone;
import com.drone.poc.models.Instruction;
import com.drone.poc.models.Sortie;
import com.drone.poc.models.enums.DroneStatus;
import com.drone.poc.models.enums.InstructionStatus;
import com.drone.poc.models.enums.SortieStatus;
import com.drone.poc.repositories.DroneRepository;
import com.drone.poc.repositories.InstructionRepository;
import com.drone.poc.repositories.SortieRepository;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class set up and undo scenario preconditions.
 */
@Component
public class TestScenarioManager {
    private static final String PATTERN_TEST_TAG = "^@T_\\d{3}$";
    @Autowired
    private Environment environment;

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private InstructionRepository instructionRepository;
    @Autowired
    private SortieRepository sortieRepository;

    public void setupPreconditions(Scenario scenario) {
        ArrayList<String> testTags =
                scenario.getSourceTagNames()
                        .stream()
                        .filter((tag) -> tag.matches(PATTERN_TEST_TAG))
                        .collect(Collectors.toCollection(ArrayList::new));

        // Throw error when no test id tags are found for scenario
        if (testTags.isEmpty()) throw new TestIdTagNotFoundException(scenario.getName(), scenario.getLine());

        String testId = testTags.get(0).substring(1);
        switch (testId) {
            case "T_001":
                // Create at least one drone in the database
                createDrone(
                        "drone_001",
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                break;
            case "T_002":
                // Create drone with id T_002_DRONE_ID in the database
                createDrone(
                        getTestData("T_002_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                break;
            case "T_003":
                // Create drone with id T_003_DRONE_ID in the database and drone is not busy
                createDrone(
                        getTestData("T_003_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                break;
            case "T_004":
                // Create instruction with id T_004_INSTRUCTION_ID in the database
                createInstruction(
                        getTestData("T_004_INSTRUCTION_ID"),
                        InstructionStatus.in_progress
                );
                break;
            case "T_005":
                // Create drone with id T_005_DRONE_ID and instruction with id T_005_INSTRUCTION_ID in the database
                // And also create a sortie record for given drone and instruction
                Drone drone = createDrone(
                        getTestData("T_005_DRONE_ID"),
                        DroneStatus.performing_task,
                        80f,
                        "Drone M21"
                );
                Instruction instruction = createInstruction(
                        getTestData("T_005_INSTRUCTION_ID"),
                        InstructionStatus.in_progress
                );
                createSortie(
                        drone,
                        instruction,
                        SortieStatus.arrived_at_warehouse,
                        "-10.20,20.30",
                        "-12.25,21.53",
                        "-10.20,20.30",
                        80.25f,
                        20.20f
                );
                break;
            case "T_006":
                // Delete all drones from the database
                deleteAllDrones();
                break;
            case "T_007":
                // Make sure drone with id T_007_DRONE_ID is not present in the database
                deleteDrone(getTestData("T_007_DRONE_ID"));
                break;
            case "T_008":
            case "T_009":
                // Make sure drone with id T_008_DRONE_ID is not present in the database
                deleteDrone(getTestData("T_008_DRONE_ID"));
                break;
            case "T_010":
            case "T_011":
                // Create drones with ids T_010_DRONE_ID and T_011_DRONE_ID in the database
                // Also drones are not busy
                createDrone(
                        getTestData("T_010_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                createDrone(
                        getTestData("T_011_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M22"
                );
                break;
            case "T_012":
            case "T_013":
                // Create drones with ids T_012_DRONE_ID and T_013_DRONE_ID in the database
                // Also drones are not busy
                createDrone(
                        getTestData("T_012_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                createDrone(
                        getTestData("T_013_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M22"
                );
                break;
            case "T_014":
            case "T_015":
                // Create drones with ids T_014_DRONE_ID and T_015_DRONE_ID in the database
                // Also drones are not busy
                createDrone(
                        getTestData("T_014_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M21"
                );
                createDrone(
                        getTestData("T_015_DRONE_ID"),
                        DroneStatus.no_task_assigned,
                        100f,
                        "Drone M22"
                );
                break;
            case "T_016":
                // Create drone with id T_016_DRONE_ID in the database and drone is busy
                createDrone(
                        getTestData("T_016_DRONE_ID"),
                        DroneStatus.performing_task,
                        100f,
                        "Drone M21"
                );
                break;
            case "T_017":
                // Make sure instruction with id T_017_INSTRUCTION_ID is not present in the database
                deleteInstruction(getTestData("T_017_INSTRUCTION_ID"));
                break;
            case "T_018":
                // Make sure drone with id T_018_DRONE_ID is not present in the database
                // Create instruction with id T_018_INSTRUCTION_ID in the database
                deleteDrone(getTestData("T_018_DRONE_ID"));
                createInstruction(
                        getTestData("T_018_INSTRUCTION_ID"),
                        InstructionStatus.in_progress
                );
                break;
            case "T_019":
                // Create drone with id T_019_DRONE_ID in the database
                // Make sure instruction with id T_019_INSTRUCTION_ID is not present in the database
                createDrone(
                        getTestData("T_019_DRONE_ID"),
                        DroneStatus.performing_task,
                        80.0f,
                        "Drone M31"
                );
                deleteInstruction(getTestData("T_019_INSTRUCTION_ID"));
                break;
        }
    }

    public void undoPreconditions(Scenario scenario) {
        ArrayList<String> testTags =
                scenario.getSourceTagNames()
                        .stream()
                        .filter((tag) -> tag.matches(PATTERN_TEST_TAG))
                        .collect(Collectors.toCollection(ArrayList::new));

        // Throw error when no test id tags are found for scenario
        if (testTags.isEmpty()) throw new TestIdTagNotFoundException(scenario.getName(), scenario.getLine());

        String testId = testTags.get(0).substring(1);
        switch (testId) {
            case "T_001":
                // Undo Precondition - Create at least one drone in the database
                deleteDrone("drone_001");
                break;
            case "T_002":
                // Undo Precondition - Create drone with id T_002_DRONE_ID in the database
                deleteDrone(getTestData("T_002_DRONE_ID"));
                break;
            case "T_003":
                // Undo Precondition - Create drone with id T_003_DRONE_ID in the database and drone is not busy
                deleteDrone(getTestData("T_003_DRONE_ID"));
                break;
            case "T_004":
                // Undo Precondition - Create instruction with id T_004_INSTRUCTION_ID in the database
                deleteInstruction(getTestData("T_004_INSTRUCTION_ID"));
                break;
            case "T_005":
                // Undo Precondition - Create drone with id T_005_DRONE_ID and instruction with id T_005_INSTRUCTION_ID in the database
                // And also create a sortie record for given drone and instruction
                deleteSortie(getTestData("T_005_DRONE_ID"), getTestData("T_005_INSTRUCTION_ID"));
                deleteDrone(getTestData("T_005_DRONE_ID"));
                deleteInstruction(getTestData("T_005_INSTRUCTION_ID"));
                break;
            case "T_006":
                // No need to Undo Precondition - Delete all drones from the database
                break;
            case "T_007":
                // No need to Undo Precondition - Make sure drone with id T_007_DRONE_ID is not present in the database
                break;
            case "T_008":
            case "T_009":
                // No need to Undo Precondition - Make sure drone with id T_008_DRONE_ID is not present in the database
                break;
            case "T_010":
            case "T_011":
                // Undo Precondition - Create drones with ids T_010_DRONE_ID and T_011_DRONE_ID in the database
                // Also drones are not busy
                deleteDrone(getTestData("T_010_DRONE_ID"));
                deleteDrone(getTestData("T_011_DRONE_ID"));
                break;
            case "T_012":
            case "T_013":
                // Undo Precondition - Create drones with ids T_012_DRONE_ID and T_013_DRONE_ID in the database
                // Also drones are not busy
                deleteDrone(getTestData("T_012_DRONE_ID"));
                deleteDrone(getTestData("T_013_DRONE_ID"));
                break;
            case "T_014":
            case "T_015":
                // Undo Precondition - Create drones with ids T_014_DRONE_ID and T_015_DRONE_ID in the database
                // Also drones are not busy
                deleteDrone(getTestData("T_014_DRONE_ID"));
                deleteDrone(getTestData("T_015_DRONE_ID"));
                break;
            case "T_016":
                // Undo Precondition - Create drone with id T_016_DRONE_ID in the database and drone is busy
                deleteDrone(getTestData("T_016_DRONE_ID"));
                break;
            case "T_017":
                // No need to Undo Precondition - Make sure instruction with id T_017_INSTRUCTION_ID is not present in the database
                break;
            case "T_018":
                // Undo Precondition - Make sure drone with id T_018_DRONE_ID is not present in the database
                // Create instruction with id T_018_INSTRUCTION_ID in the database
                deleteInstruction(getTestData("T_018_INSTRUCTION_ID"));
                break;
            case "T_019":
                // Undo Precondition - Create drone with id T_019_DRONE_ID in the database
                // Make sure instruction with id T_019_INSTRUCTION_ID is not present in the database
                deleteDrone(getTestData("T_019_DRONE_ID"));
                break;
        }
    }

    private String getTestData(String testDataKey) {
        String value = environment.getProperty(testDataKey);
        if (value == null) throw new TestDataNotFoundException(testDataKey);
        return value;
    }

    private Drone createDrone(String droneId, DroneStatus droneStatus, float chargeLevel, String model) {
        Drone drone = droneRepository.findByDroneId(droneId).orElse(null);
        if (drone == null) drone = new Drone();
        drone.setDroneId(droneId);
        drone.setStatus(droneStatus);
        drone.setChargeLevel(chargeLevel);
        drone.setModel(model);
        drone = droneRepository.save(drone);
        return drone;
    }

    private void deleteDrone(String droneId) {
        droneRepository.deleteByDroneId(droneId);
    }

    private void deleteAllDrones() {
        droneRepository.deleteAll();
    }

    private Instruction createInstruction(String instructionId, InstructionStatus instructionStatus) {
        Instruction instruction = instructionRepository.findByInstructionId(instructionId).orElse(null);
        if (instruction == null) instruction = new Instruction();
        instruction.setInstructionId(instructionId);
        instruction.setStatus(instructionStatus);
        instruction = instructionRepository.save(instruction);
        return instruction;
    }

    private void deleteInstruction(String instructionId) {
        instructionRepository.deleteByInstructionId(instructionId);
    }

    private Sortie createSortie(Drone drone, Instruction instruction, SortieStatus sortieStatus, String warehouseLocation, String destinationLocation, String currentLocation, float etaInMinutes, float currentSpeedInKmph) {
        List<Sortie> sortieList = sortieRepository.findBy(drone, instruction);
        Sortie sortie = sortieList.isEmpty() ? new Sortie() : sortieList.get(0);
        sortie.setInstruction(instruction);
        sortie.setDrone(drone);
        sortie.setStatus(sortieStatus);
        sortie.setWarehouseLocation(warehouseLocation);
        sortie.setDestinationLocation(destinationLocation);
        sortie.setCurrentLocation(currentLocation);
        sortie.setEtaInMinutes(etaInMinutes);
        sortie.setCurrentSpeedInKmph(currentSpeedInKmph);
        sortie = sortieRepository.save(sortie);
        return sortie;
    }

    private void deleteSortie(String droneId, String instructionId) {
        Drone drone = droneRepository.findByDroneId(droneId).orElse(null);
        Instruction instruction = instructionRepository.findByInstructionId(instructionId).orElse(null);
        if (drone == null || instruction == null) return;
        sortieRepository.deleteBy(drone, instruction);
    }
}
