package com.example.dronepoc.controllers;

import com.example.dronepoc.exception.DroneNotFoundException;
import com.example.dronepoc.exception.EmptyDroneListException;
import com.example.dronepoc.exception.SortieUpdateException;
import com.example.dronepoc.models.Drone;
import com.example.dronepoc.models.Instruction;
import com.example.dronepoc.models.Sortie;
import com.example.dronepoc.models.enums.DroneStatus;
import com.example.dronepoc.models.enums.InstructionStatus;
import com.example.dronepoc.models.enums.SortieStatus;
import com.example.dronepoc.repositories.DroneRepository;
import com.example.dronepoc.repositories.InstructionRepository;
import com.example.dronepoc.repositories.SortieRepository;
import com.example.dronepoc.requests.MutateDataRequestBody;
import com.example.dronepoc.requests.data.SortieUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DroneController {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private InstructionRepository instructionRepository;

    @Autowired
    private SortieRepository sortieRepository;

    @GetMapping(value = "/api/drones")
    public List<Drone> listDrones() {
        List<Drone> drones = droneRepository.findAll();
        if (drones.isEmpty()) throw new EmptyDroneListException();
        return drones;
    }

    @GetMapping("/api/drones/{droneId}")
    public Drone getDroneDetails(@PathVariable long droneId) {
        return droneRepository.findById(droneId)
                .orElseThrow(DroneNotFoundException::new);
    }

    @PutMapping("/api/drones/{droneId}/sorties")
    public String updateSortieStatus(@PathVariable long droneId, @RequestBody MutateDataRequestBody<SortieUpdateData> data) {
        SortieUpdateData updateData = data.getPayload().get(0);
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new SortieUpdateException("drone_id not found"));
        Instruction instruction = instructionRepository.findById(Long.parseLong(updateData.getInstructionId())).orElseThrow(() -> new SortieUpdateException("instruction_id not found"));
        Sortie sortie = sortieRepository.findBy(drone, instruction, null).get(0);
        sortie.setCurrentLocation(updateData.getCurrentLocation());
        sortie.setStatus(updateData.getStatus());
        sortie.setDestinationLocation(updateData.getDestinationLocation());
        sortie.setWarehouseLocation(updateData.getWarehouseLocation());
        sortie.setEtaInMinutes(Float.parseFloat(updateData.getEtaInMinutes()));
        sortie.setCurrentSpeedInKmph(Float.parseFloat(updateData.getCurrentSpeedInKmph()));
        sortie = sortieRepository.save(sortie);
        // Also update drone and instruction
        if (sortie.getStatus() == SortieStatus.in_progress) {
            if (drone.getStatus() != DroneStatus.performing_task)
                drone.setStatus(DroneStatus.performing_task);
            if (instruction.getStatus() != InstructionStatus.in_progress)
                instruction.setStatus(InstructionStatus.in_progress);
        } else if (sortie.getStatus() == SortieStatus.delivered) {
            if (drone.getStatus() != DroneStatus.no_task_assigned)
                drone.setStatus(DroneStatus.no_task_assigned);
            if (instruction.getStatus() != InstructionStatus.delivered) {
                instruction.setStatus(InstructionStatus.delivered);
            }
        }

        return String.format("{\"drone_id\":\"%s\",\"instruction_id\":\"%s\",\"status\":\"%s\"}", droneId, updateData.getInstructionId(), sortie.getStatus());
    }
}
