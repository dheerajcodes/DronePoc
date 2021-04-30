package com.example.dronepoc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DroneNotFoundException extends RuntimeException {
    @Getter
    private final long droneId;
}
