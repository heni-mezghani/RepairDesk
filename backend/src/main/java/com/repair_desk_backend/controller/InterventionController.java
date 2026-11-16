package com.repair_desk_backend.controller;

import com.repair_desk_backend.entity.Intervention;
import com.repair_desk_backend.service.InterventionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines/{machineId}/interventions")
public class InterventionController {

    private final InterventionService interventionService;

    public InterventionController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    public List<Intervention> findByMachine(@PathVariable Long machineId) {
        return interventionService.findByMachineId(machineId);
    }

    @PostMapping
    public ResponseEntity<Intervention> create(@PathVariable Long machineId,
                                               @Valid @RequestBody Intervention intervention) {
        return interventionService.save(machineId, intervention);
    }
}