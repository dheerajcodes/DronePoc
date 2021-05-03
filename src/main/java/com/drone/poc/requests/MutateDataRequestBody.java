package com.drone.poc.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MutateDataRequestBody<T> {
    @JsonProperty("data")
    private RequestData<T> data;

    public List<T> getPayload() {
        return Arrays.asList(data.getPayload());
    }
}
