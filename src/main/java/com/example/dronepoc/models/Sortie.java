package com.example.dronepoc.models;

import com.example.dronepoc.models.enums.SortieStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sortie",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"drone_id", "instruction_id"})
        }
)
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Sortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drone_id", nullable = false)
    @Getter
    @Setter
    private Drone drone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instruction_id", nullable = false)
    @Getter
    @Setter
    private Instruction instruction;

    @Column(nullable = false)
    @Getter
    @Setter
    private String currentLocation;

    @Column(nullable = false)
    @Getter
    @Setter
    private String destinationLocation;

    @Column(nullable = false)
    @Getter
    @Setter
    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private SortieStatus status;

    @Column(nullable = false)
    @Getter
    @Setter
    private float etaInMinutes;

    @Column(nullable = false)
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
