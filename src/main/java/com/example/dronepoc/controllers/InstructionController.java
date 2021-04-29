package com.example.dronepoc.controllers;


import com.example.dronepoc.exception.DroneBusyException;
import com.example.dronepoc.exception.DroneNotFoundException;
import com.example.dronepoc.exception.InstructionNotFoundException;
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
import com.example.dronepoc.requests.data.NewInstructionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InstructionController {
    @Autowired
    InstructionRepository instructionRepository;

    @Autowired
    SortieRepository sortieRepository;

    @Autowired
    DroneRepository droneRepository;

    @PostMapping("/api/drones/instructions")
    public String newPickupInstruction(@RequestBody MutateDataRequestBody<NewInstructionData> data) {
        NewInstructionData payload = data.getPayload().get(0);
        // Create new instruction
        Instruction newInstruction = new Instruction();
        newInstruction.setStatus(InstructionStatus.in_progress); // Initial status for instruction
        newInstruction = instructionRepository.save(newInstruction);

        Drone drone = droneRepository
                .findById(Long.parseLong(payload.getDroneId()))
                .orElseThrow(DroneNotFoundException::new);
        // Make sure drone is ready to execute instruction
        if (drone.getStatus() == DroneStatus.performing_task)
            throw new DroneBusyException();
        // Change drone status
        drone.setStatus(DroneStatus.performing_task);
        drone = droneRepository.save(drone);

        // Todo invalid field and empty field exceptions

        // Create new sortie instruction
        Sortie newSortie = new Sortie();
        newSortie.setDrone(drone);
        newSortie.setInstruction(newInstruction);
        newSortie.setStatus(SortieStatus.in_progress); // Initial status for sortie
        newSortie.setCurrentLocation("0,0"); // Initial current location of drone
        newSortie.setDestinationLocation(payload.getDestinationLocation());
        newSortie.setWarehouseLocation(payload.getWarehouseLocation());
        newSortie.setEtaInMinutes(-1);
        newSortie.setCurrentSpeedInKmph(0);
        sortieRepository.save(newSortie);

        return String.format(
                "{\"drone_id\":\"%d\",\"instruction_id\":\"%d\",\"delivery_status\":\"%s\"}",
                drone.getId(),
                newInstruction.getId(),
                newInstruction.getStatus()
        );
    }

    @GetMapping("/api/drones/instructions/{instructionId}")
    public Instruction getInstructionStatus(@PathVariable long instructionId) {
        return instructionRepository
                .findById(instructionId)
                .orElseThrow(InstructionNotFoundException::new);
    }
}
