package com.example.dronepoc.controllers;

import com.example.dronepoc.models.Drone;
import com.example.dronepoc.repositories.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DroneController {
    @Autowired
    private DroneRepository repository;

    @GetMapping(value = "/api/drones")
    public List<Drone> listDrones() {
        return repository.findAll();
    }

    @GetMapping("/api/drones/{droneId}")
    public String getDroneDetails(@PathVariable int droneId) {
        return "{}";
    }

    @PutMapping("/api/drones/{droneId}/sorties")
    public String updateSortieStatus(@PathVariable int droneId, @RequestBody String data) {
        return "{success:true}";
    }
}
