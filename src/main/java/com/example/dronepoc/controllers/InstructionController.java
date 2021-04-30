package com.example.dronepoc.controllers;

import com.example.dronepoc.exceptions.*;
import com.example.dronepoc.models.Drone;
import com.example.dronepoc.models.Instruction;
import com.example.dronepoc.models.Sortie;
import com.example.dronepoc.models.enums.DroneStatus;
import com.example.dronepoc.models.enums.InstructionStatus;
import com.example.dronepoc.models.enums.SortieStatus;
import com.example.dronepoc.repositories.DroneRepository;
import com.example.dronepoc.repositories.InstructionRepository;
import com.example.dronepoc.repositories.SortieRepository;
import com.example.dronepoc.requests.MutateDataRequestBody;
import com.example.dronepoc.requests.data.NewInstructionData;
import com.example.dronepoc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class InstructionController {
    @Autowired
    InstructionRepository instructionRepository;

    @Autowired
    SortieRepository sortieRepository;

    @Autowired
    DroneRepository droneRepository;

    private static final String RESPONSE_WITH_STATUS_AND_DATA = "{\"status\":%d,\"data\":%s}";

    private static final String RESPONSE_WITH_DATA = "{\"data\":%s}";

    private static final String MSG_FIELD_EMPTY = "%s cannot be empty.";
    private static final String MSG_FIELD_INVALID = "%s is in invalid format.";

    private static final String ERROR_TEMPLATE_JSON = "{\"errors\":[{\"status\":%d,\"code\":\"%s\",\"detail\":\"%s\"}]}";
    private static final String ERROR_TEMPLATE_STATUS_JSON = "{\"status\":%d,\"errors\":[{\"code\":\"%s\",\"detail\":\"%s\"}]}";

    @PostMapping("/api/drones/instructions")
    public ResponseEntity<String> newPickupInstruction(@RequestBody MutateDataRequestBody<NewInstructionData> data) {
        NewInstructionData payload = data.getPayload().get(0);

        // Validate Payload Data
        long droneId;
        try {
            if (payload.getDroneId().trim().isEmpty())
                throw new EmptyFieldException(String.format(MSG_FIELD_EMPTY, "drone_id"));
            droneId = Long.parseLong(payload.getDroneId());
        } catch (NumberFormatException exception) {
            throw new InvalidFieldException(String.format(MSG_FIELD_INVALID, "drone_id"));
        }

        String instructionType = payload.getType().trim();
        if (instructionType.isEmpty()) throw new EmptyFieldException(String.format(MSG_FIELD_EMPTY, "type"));
        else if (!instructionType.equals("pickup"))
            throw new InvalidFieldException(String.format(MSG_FIELD_INVALID, "type"));

        String warehouseLocation = Utilities.removeWhitespaces(payload.getWarehouseLocation());
        if (warehouseLocation.isEmpty()) throw new EmptyFieldException(String.format(MSG_FIELD_EMPTY, "warehouse_loc"));
        if (!Utilities.validateLocationCoordinates(warehouseLocation))
            throw new InvalidFieldException(String.format(MSG_FIELD_INVALID, "warehouse_loc"));

        String destinationLocation = Utilities.removeWhitespaces(payload.getDestinationLocation());
        if (destinationLocation.isEmpty())
            throw new EmptyFieldException(String.format(MSG_FIELD_EMPTY, "destination_loc"));
        if (!Utilities.validateLocationCoordinates(destinationLocation))
            throw new InvalidFieldException(String.format(MSG_FIELD_INVALID, "destination_loc"));

        // Make sure drone is ready
        Drone drone = droneRepository
                .findById(droneId)
                .orElseThrow(() -> new DroneNotFoundException(droneId));

        if (drone.getStatus() == DroneStatus.performing_task)
            throw new DroneBusyException();

        // Create new instruction
        Instruction newInstruction = new Instruction();
        newInstruction.setStatus(InstructionStatus.in_progress); // Initial status for instruction
        newInstruction = instructionRepository.save(newInstruction);

        // Create new sortie instruction
        Sortie newSortie = new Sortie();
        newSortie.setDrone(drone);
        newSortie.setInstruction(newInstruction);
        newSortie.setStatus(SortieStatus.in_progress); // Initial status for sortie
        newSortie.setCurrentLocation("0,0"); // Initial current location of drone
        newSortie.setDestinationLocation(destinationLocation);
        newSortie.setWarehouseLocation(warehouseLocation);
        newSortie.setEtaInMinutes(-1);
        newSortie.setCurrentSpeedInKmph(0);
        sortieRepository.save(newSortie);

        // Change drone status
        drone.setStatus(DroneStatus.performing_task);
        drone = droneRepository.save(drone);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        String responseBody = String.format("{\"drone_id\":\"%d\",\"instruction_id\":\"%d\",\"delivery_status\":\"%s\"}",
                drone.getId(),
                newInstruction.getId(),
                newInstruction.getStatus().toString()
        );
        responseBody = String.format(RESPONSE_WITH_STATUS_AND_DATA, HttpStatus.OK.value(), responseBody);
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @GetMapping("/api/drones/instructions/{instructionId}")
    public ResponseEntity<String> getInstructionStatus(@PathVariable long instructionId) throws IOException {
        Instruction instruction = instructionRepository
                .findById(instructionId)
                .orElseThrow(() -> new InstructionNotFoundException(instructionId));
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        String responseBody = String.format(RESPONSE_WITH_DATA, Utilities.objectToJson(instruction));
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<String> handleEmptyFieldException(EmptyFieldException exception) {
        String responseBody = String.format(
                ERROR_TEMPLATE_STATUS_JSON,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "validation_error",
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException exception) {
        String responseBody = String.format(
                ERROR_TEMPLATE_STATUS_JSON,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "validation_error",
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(DroneBusyException.class)
    public ResponseEntity<String> handleDroneBusyException() {
        String responseBody = String.format(
                ERROR_TEMPLATE_STATUS_JSON,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "error",
                "Drone is busy."
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @ExceptionHandler(InstructionNotFoundException.class)
    public ResponseEntity<String> handleInstructionNotFoundException(InstructionNotFoundException exception) {
        String responseBody = String.format(
                ERROR_TEMPLATE_JSON,
                HttpStatus.NOT_FOUND.value(),
                "not_found",
                "Instruction with id=" + exception.getInstructionId() + " not found"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }
}
