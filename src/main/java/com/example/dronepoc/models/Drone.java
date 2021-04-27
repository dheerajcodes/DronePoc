package com.example.dronepoc.models;

import com.example.dronepoc.models.enums.DroneStatus;
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
    @Getter
    @Setter
    private float chargeLevel;

    @Column(nullable = false)
    @Getter
    @Setter
    private String model;

    @OneToMany(mappedBy = "drone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Sortie> sorties;

    public Drone(DroneStatus status, float chargeLevel, String model) {
        this.status = status;
        this.chargeLevel = chargeLevel;
        this.model = model;
    }
}
