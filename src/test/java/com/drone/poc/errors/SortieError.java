package com.drone.poc.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum SortieError {

    DroneIdNotFound("not_found", "drone_id not found"),
    InstructionIdNotFound("not_found", "instruction_id not found");

    @Getter
    private final String code;
    @Getter
    private final String detail;
}
