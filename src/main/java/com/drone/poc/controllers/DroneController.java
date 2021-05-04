package com.drone.poc.controllers;

import com.drone.poc.models.enums.DroneStatus;
import com.drone.poc.models.enums.SortieStatus;
import com.drone.poc.exceptions.DroneNotFoundException;
import com.drone.poc.exceptions.EmptyDroneListException;
import com.drone.poc.exceptions.SortieUpdateException;
import com.drone.poc.models.Drone;
import com.drone.poc.models.Instruction;
import com.drone.poc.models.Sortie;
import com.drone.poc.models.enums.InstructionStatus;
import com.drone.poc.repositories.DroneRepository;
import com.drone.poc.repositories.InstructionRepository;
import com.drone.poc.repositories.SortieRepository;
import com.drone.poc.requests.MutateDataRequestBody;
import com.drone.poc.requests.data.SortieUpdateData;
import com.drone.poc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class DroneController {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private InstructionRepository instructionRepository;

    @Autowired
    private SortieRepository sortieRepository;

    private static final String RESPONSE_WITH_STATUS_AND_DATA = "{\"status\":%d,\"data\":%s}";
    private static final String RESPONSE_WITH_DATA = "{\"data\":%s}";

    private static final String ERROR_TEMPLATE_JSON = "{\"errors\":[{\"status\":%d,\"code\":\"%s\",\"detail\":\"%s\"}]}";

    @GetMapping(value = "/api/drones")
    public ResponseEntity<String> listDrones() throws IOException {
        List<Drone> drones = droneRepository.findAll();
        if (drones.isEmpty()) throw new EmptyDroneListException();
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        String responseBody = String.format(RESPONSE_WITH_STATUS_AND_DATA, HttpStatus.OK.value(), Utilities.objectToJson(drones));
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @GetMapping("/api/drones/{droneId}")
    public ResponseEntity<String> getDroneDetails(@PathVariable String droneId) throws IOException {
        Drone drone = droneRepository.findByDroneId(droneId)
                .orElseThrow(() -> new DroneNotFoundException(droneId));
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        String responseBody = String.format(RESPONSE_WITH_DATA, Utilities.objectToJson(drone));
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @PutMapping("/api/drones/{droneId}/sorties")
    public ResponseEntity<String> updateSortieStatus(@PathVariable String droneId, @RequestBody MutateDataRequestBody<SortieUpdateData> data) {
        SortieUpdateData updateData = data.getPayload().get(0);
        Drone drone = droneRepository.findByDroneId(droneId).orElseThrow(() -> new SortieUpdateException("drone_id not found"));
        Instruction instruction = instructionRepository.findByInstructionId(updateData.getInstructionId()).orElseThrow(() -> new SortieUpdateException("instruction_id not found"));
        Sortie sortie = sortieRepository.findBy(drone, instruction).get(0);
        sortie.setCurrentLocation(updateData.getCurrentLocation());
        sortie.setStatus(updateData.getStatus());
        sortie.setDestinationLocation(updateData.getDestinationLocation());
        sortie.setWarehouseLocation(updateData.getWarehouseLocation());
        sortie.setEtaInMinutes(Float.parseFloat(updateData.getEtaInMinutes()));
        sortie.setCurrentSpeedInKmph(Float.parseFloat(updateData.getCurrentSpeedInKmph()));
        sortie = sortieRepository.save(sortie);
        // Also update drone and instruction
        if (sortie.getStatus() == SortieStatus.in_progress) {
            if (drone.getStatus() != DroneStatus.performing_task)
                drone.setStatus(DroneStatus.performing_task);
            if (instruction.getStatus() != InstructionStatus.in_progress)
                instruction.setStatus(InstructionStatus.in_progress);
        } else if (sortie.getStatus() == SortieStatus.delivered) {
            if (drone.getStatus() != DroneStatus.no_task_assigned)
                drone.setStatus(DroneStatus.no_task_assigned);
            if (instruction.getStatus() != InstructionStatus.delivered) {
                instruction.setStatus(InstructionStatus.delivered);
            }
        }

        droneRepository.save(drone);
        instructionRepository.save(instruction);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        String responseBody = String.format(
                "{\"drone_id\":\"%s\",\"instruction_id\":\"%s\",\"status\":\"%s\"}",
                drone.getDroneId(),
                instruction.getInstructionId(),
                "updated"
        );
        responseBody = String.format(RESPONSE_WITH_STATUS_AND_DATA, HttpStatus.NO_CONTENT.value(), responseBody);
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(EmptyDroneListException.class)
    public ResponseEntity<String> handleEmptyDroneListException() {
        String responseBody = String.format(ERROR_TEMPLATE_JSON, HttpStatus.NOT_FOUND.value(), "resource_not_found", "Requested resource not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<String> handleDroneNotFoundException(DroneNotFoundException exception) {
        String responseBody = String.format(ERROR_TEMPLATE_JSON, HttpStatus.NOT_FOUND.value(), "not_found", "drone with id=" + exception.getDroneId() + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(SortieUpdateException.class)
    public ResponseEntity<String> handleSortieUpdateException(SortieUpdateException exception) {
        String responseBody = String.format(ERROR_TEMPLATE_JSON, HttpStatus.NOT_FOUND.value(), "not_found", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }
}
