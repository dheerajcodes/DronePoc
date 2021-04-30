package com.example.dronepoc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InstructionNotFoundException extends RuntimeException {
    @Getter
    long instructionId;
}
