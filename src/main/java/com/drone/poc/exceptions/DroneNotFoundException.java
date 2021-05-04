package com.drone.poc.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DroneNotFoundException extends RuntimeException {
    @Getter
    private final String droneId;
}
