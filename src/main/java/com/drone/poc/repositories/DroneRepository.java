package com.drone.poc.repositories;

import com.drone.poc.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findByDroneId(String droneId);

    void deleteByDroneId(String droneId);
}
