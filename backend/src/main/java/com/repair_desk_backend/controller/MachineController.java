package com.repair_desk_backend.controller;

import com.repair_desk_backend.entity.Machine;
import com.repair_desk_backend.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public List<Machine> findAll() {
        return machineService.findAll();
    }

    @PutMapping("/{id}")
    public Machine update(@PathVariable Long id,@Valid @RequestBody Machine machine) {
        return machineService.update(id, machine);
    }

    @GetMapping("/{id}")
    public Machine findById(@PathVariable Long id) {
        return machineService.findById(id);
    }

}
