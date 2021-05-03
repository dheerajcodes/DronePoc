package com.drone.poc.models;

import com.drone.poc.models.enums.DroneStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "drone")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private DroneStatus status;

    @Column(nullable = false)
    @JsonProperty("charge_level")
    @Getter
    @Setter
    private float chargeLevel;

    @Column(nullable = false)
    @Getter
    @Setter
    private String model;

    @OneToMany(mappedBy = "drone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Sortie> sorties;

    public Drone(DroneStatus status, float chargeLevel, String model) {
        this.status = status;
        this.chargeLevel = chargeLevel;
        this.model = model;
    }
}
