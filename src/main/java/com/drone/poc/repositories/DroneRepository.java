package com.drone.poc.repositories;

import com.drone.poc.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findByDroneId(String droneId);
}
