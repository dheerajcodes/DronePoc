package com.example.dronepoc.repositories;

import com.example.dronepoc.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, Long> {
}
