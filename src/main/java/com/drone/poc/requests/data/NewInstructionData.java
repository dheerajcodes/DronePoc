package com.drone.poc.requests.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewInstructionData {
    @JsonProperty("drone_id")
    @Getter
    @Setter
    private String droneId;

    @JsonProperty("warehouse_loc")
    @Getter
    @Setter
    private String warehouseLocation;

    @JsonProperty("type")
    @Getter
    @Setter
    private String type;

    @JsonProperty("destination_loc")
    @Getter
    @Setter
    private String destinationLocation;
}
