package com.drone.poc.requests.data;

import com.drone.poc.models.enums.SortieStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SortieUpdateData {
    @JsonProperty("instruction_id")
    @Getter
    @Setter
    private String instructionId;

    @JsonProperty("current_loc")
    @Getter
    @Setter
    private String currentLocation;

    @JsonProperty("destination_loc")
    @Getter
    @Setter
    private String destinationLocation;

    @JsonProperty("warehouse_loc")
    @Getter
    @Setter
    private String warehouseLocation;

    @JsonProperty("status")
    @Getter
    @Setter
    private SortieStatus status;

    @JsonProperty("estimated_time_of_arrival_in_min")
    @Getter
    @Setter
    private String etaInMinutes;

    @JsonProperty("current_speed_in_kmph")
    @Getter
    @Setter
    private String currentSpeedInKmph;
}
