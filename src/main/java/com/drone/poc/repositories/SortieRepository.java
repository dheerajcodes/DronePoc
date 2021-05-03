package com.drone.poc.repositories;

import com.drone.poc.models.Drone;
import com.drone.poc.models.Instruction;
import com.drone.poc.models.Sortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SortieRepository extends JpaRepository<Sortie, Long> {
    @Query("SELECT s FROM Sortie s WHERE s.drone = ?1 AND s.instruction = ?2")
    List<Sortie> findBy(Drone drone, Instruction instruction);
}