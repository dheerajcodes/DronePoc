package com.drone.poc.models;

import com.drone.poc.models.enums.InstructionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "instruction")
@NoArgsConstructor
@ToString
@JsonPropertyOrder({"id", "status"})
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @JsonIgnore
    private long primaryKey;

    @Column(name = "ins_id", unique = true)
    @JsonProperty("id")
    @Getter
    @Setter
    private String instructionId;

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
        this.status = status;
    }

    public static String makeInstructionId(Instruction instruction) {
        return String.format("ins_%d", instruction.getPrimaryKey());
    }
}
