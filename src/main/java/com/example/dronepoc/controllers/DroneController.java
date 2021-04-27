package com.example.dronepoc.controllers;

import com.example.dronepoc.repositories.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DroneController {
    @Autowired
    private DroneRepository repository;

    @GetMapping("/api/drones")
    @ResponseBody
    public String listDrones() {
        return repository.findAll().toString();
    }

    @GetMapping("/api/drones/{droneId}")
    @ResponseBody
    public String getDroneDetails(@PathVariable int droneId) {
        return "{}";
    }

    @PutMapping("/api/drones/{droneId}/sorties")
    @ResponseBody
    public String updateSortieStatus(@PathVariable int droneId, @RequestBody String data) {
        return "{success:true}";
    }
}
