package com.example.dronepoc.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
public class InstructionController {

    @PostMapping("/api/drones/instructions")
    public String newPickupInstruction(@RequestBody String data) {
        return "{success:true}";
    }

    @GetMapping("/api/drones/instructions/{instructionId}")
    public String getInstructionStatus(@PathVariable int instructionId) {
        return "{}";
    }
}
