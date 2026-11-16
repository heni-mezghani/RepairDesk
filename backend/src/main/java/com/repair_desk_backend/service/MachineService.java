package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.Machine;
import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.MachineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineService {
    private final MachineRepository machineRepository;
    private final AuditLogService auditLogService;



    public MachineService(MachineRepository machineRepository,AuditLogService auditLogService) {
        this.machineRepository = machineRepository;
        this.auditLogService = auditLogService;
    }
    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public Machine update(Long id, Machine machine) {
        Machine existing = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine introuvable : " + id));

        machine.setId(id);
        machine.setFicheReception(existing.getFicheReception());

        auditLogService.log("UPDATE", "Machine", "ID: " + id + "\nMarque: " + existing.getMarque() + "\nType : " + existing.getType() + "\nModele : " + existing.getModele() + "\nnumero serie : " + existing.getNumeroSerie() + "\nProbleme : " + existing.getProblemeDeclare() + "\nDiagnostic : " + existing.getDiagnostic() + "\nCategorie : " + existing.getCategoriePanne() + "\nStatut : " + existing.getStatut() + "\nCoût Estimé : " + existing.getCoutEstime() + "\nCout Final : " + existing.getCoutFinal() +
                "\n→" + "\nMarque: " + machine.getMarque() + "\nType : " + machine.getType() + "\nModele : " + machine.getModele() + "\nnumero serie : " + machine.getNumeroSerie() + "\nProbleme : " + machine.getProblemeDeclare() + "\nDiagnostic : " + machine.getDiagnostic() + "\nCategorie : " + machine.getCategoriePanne() + "\nStatut : " + machine.getStatut() + "\nCoût Estimé : " + machine.getCoutEstime() + "\nCout Final : " + machine.getCoutFinal());

        return machineRepository.save(machine);
    }

    public Machine findById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Machine introuvable : " + id));
    }

}
