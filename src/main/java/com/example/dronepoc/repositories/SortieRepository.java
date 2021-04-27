package com.example.dronepoc.repositories;

import com.example.dronepoc.models.Drone;
import com.example.dronepoc.models.Instruction;
import com.example.dronepoc.models.Sortie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SortieRepository extends JpaRepository<Sortie, Long> {
    List<Sortie> findBy(Drone drone, Sort sort);

    List<Sortie> findBy(Instruction instruction, Sort sort);

    List<Sortie> findBy(Drone drone, Instruction instruction, Sort sort);
}
