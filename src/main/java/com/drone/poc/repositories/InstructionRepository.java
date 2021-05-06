package com.drone.poc.repositories;

import com.drone.poc.models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    Optional<Instruction> findByInstructionId(String instructionId);

    void deleteByInstructionId(String instructionId);
}
