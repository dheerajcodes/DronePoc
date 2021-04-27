package com.example.dronepoc.repositories;

import com.example.dronepoc.models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {
}
