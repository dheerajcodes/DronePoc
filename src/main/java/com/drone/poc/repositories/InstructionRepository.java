package com.drone.poc.repositories;

import com.drone.poc.models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    Optional<Instruction> findByInstructionId(String instructionId);
}
