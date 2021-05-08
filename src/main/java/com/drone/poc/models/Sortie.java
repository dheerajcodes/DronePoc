package com.drone.poc.models;

import com.drone.poc.models.enums.SortieStatus;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sortie",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"drone_id", "instruction_id"})
        }
)
@NoArgsConstructor
@ToString
public class Sortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Getter
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drone_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("drone_id")
    @Getter
    @Setter
    private Drone drone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instruction_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("instruction_id")
    @Getter
    @Setter
    private Instruction instruction;

    @Column(nullable = false)
    @JsonProperty("current_loc")
    @Getter
    @Setter
    private String currentLocation;

    @Column(nullable = false)
    @JsonProperty("destination_loc")
    @Getter
    @Setter
    private String destinationLocation;

    @Column(nullable = false)
    @JsonProperty("warehouse_loc")
    @Getter
    @Setter
    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private SortieStatus status;

    @Column(nullable = false)
    @JsonProperty("estimated_time_of_arrival_in_min")
    @Getter
    @Setter
    private float etaInMinutes;

    @Column(nullable = false)
    @JsonProperty("current_speed_in_kmph")
    @Getter
    @Setter
    private float currentSpeedInKmph;

    public Sortie(Drone drone, Instruction instruction, String currentLocation, String destinationLocation, String warehouseLocation, SortieStatus status) {
        this.id = -1;
        this.drone = drone;
        this.instruction = instruction;
        this.currentLocation = currentLocation;
        this.destinationLocation = destinationLocation;
        this.warehouseLocation = warehouseLocation;
        this.status = status;
        this.etaInMinutes = -1;
        this.currentSpeedInKmph = -1;
    }
}
