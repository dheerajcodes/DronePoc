package com.drone.poc.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum InstructionError {

    InvalidDroneId("validation_error", "drone_id is in invalid format."),
    EmptyDroneId("validation_error", "drone_id cannot be empty."),
    InvalidWarehouseLocation("validation_error", "warehouse_loc is in invalid format."),
    EmptyWarehouseLocation("validation_error", "warehouse_loc cannot be empty."),
    InvalidDestinationLocation("validation_error", "destination_loc is in invalid format."),
    EmptyDestinationLocation("validation_error", "destination_loc cannot be empty."),
    UnknownInstructionType("validation_error", "type is in invalid format."),
    EmptyInstructionType("validation_error", "type cannot be empty."),
    BusyDrone("error", "Drone is busy.");

    @Getter
    private final String code;
    @Getter
    private final String detail;
}
