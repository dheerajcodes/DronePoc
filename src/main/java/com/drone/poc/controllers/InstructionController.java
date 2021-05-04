package com.drone.poc.controllers;

import com.drone.poc.exceptions.*;
import com.drone.poc.models.Instruction;
import com.drone.poc.models.enums.DroneStatus;
import com.drone.poc.models.enums.SortieStatus;
import com.drone.poc.requests.MutateDataRequestBody;
import com.drone.poc.requests.data.NewInstructionData;
import com.drone.poc.utils.Utilities;
import com.drone.poc.models.Drone;
import com.drone.poc.models.Sortie;
import com.drone.poc.models.enums.InstructionStatus;
import com.drone.poc.repositories.DroneRepository;
import com.drone.poc.repositories.InstructionRepository;
import com.drone.poc.repositories.SortieRepository;
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
        Drone drone;
        try {
            if (payload.getDroneId().trim().isEmpty())
                throw new EmptyFieldException(String.format(MSG_FIELD_EMPTY, "drone_id"));
            String droneId = payload.getDroneId();
            drone = droneRepository.findByDroneId(droneId).orElseThrow(() -> new DroneNotFoundException(droneId));
        } catch (DroneNotFoundException exception) {
            throw new InvalidFieldException(String.format(MSG_FIELD_INVALID, "drone_id"));
        }
        // Make sure drone is ready
        if (drone.getStatus() == DroneStatus.performing_task)
            throw new DroneBusyException();

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

        // Create new instruction with initial status
        Instruction newInstruction = new Instruction(InstructionStatus.in_progress);
        newInstruction = instructionRepository.save(newInstruction);
        // Create and save instruction id from its newly assigned primary key value
        newInstruction.setInstructionId(Instruction.makeInstructionId(newInstruction));
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
        String responseBody = String.format("{\"drone_id\":\"%s\",\"instruction_id\":\"%s\",\"delivery_status\":\"%s\"}",
                drone.getDroneId(),
                newInstruction.getInstructionId(),
                newInstruction.getStatus().toString()
        );
        responseBody = String.format(RESPONSE_WITH_STATUS_AND_DATA, HttpStatus.OK.value(), responseBody);
        return responseBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @GetMapping("/api/drones/instructions/{instructionId}")
    public ResponseEntity<String> getInstructionStatus(@PathVariable String instructionId) throws IOException {
        Instruction instruction = instructionRepository
                .findByInstructionId(instructionId)
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
