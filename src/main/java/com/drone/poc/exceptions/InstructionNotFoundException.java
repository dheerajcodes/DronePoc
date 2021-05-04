package com.drone.poc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InstructionNotFoundException extends RuntimeException {
    @Getter
    String instructionId;
}
