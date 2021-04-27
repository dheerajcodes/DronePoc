package com.example.dronepoc.models;

import com.example.dronepoc.models.enums.InstructionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "instruction")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private InstructionStatus status;

    @OneToMany(mappedBy = "instruction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Sortie> sorties;

    public Instruction(InstructionStatus status) {
        this.id = -1;
        this.status = status;
    }
}
