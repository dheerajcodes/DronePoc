package com.drone.poc.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestData<T> {
    @JsonProperty("payload")
    @Getter
    private T[] payload;
}
