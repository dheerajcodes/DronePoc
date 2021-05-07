package com.drone.poc.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@ToString
public enum DroneError {

    DroneListNotFound(
            HttpStatus.NOT_FOUND.value(),
            "resource_not_found",
            "Requested resource not found.",
            false
    ),
    DroneDetailNotFound(
            HttpStatus.NOT_FOUND.value(),
            "not_found",
            "drone with id=%s not found",
            true
    );

    @Getter
    private final int status;
    @Getter
    private final String code;
    @Getter
    private final String detail;
    @Getter
    private final boolean detailHasDroneId;
}
