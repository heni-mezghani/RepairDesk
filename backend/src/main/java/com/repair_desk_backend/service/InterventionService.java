package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.Intervention;
import com.repair_desk_backend.entity.Machine;
import com.repair_desk_backend.entity.Piece;
import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.InterventionRepository;
import com.repair_desk_backend.repository.MachineRepository;
import com.repair_desk_backend.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InterventionService {

    private final InterventionRepository interventionRepository;
    private final MachineRepository machineRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AuditLogService auditLogService;


    public InterventionService(InterventionRepository interventionRepository,
                               MachineRepository machineRepository,
                               UtilisateurRepository utilisateurRepository,
                               AuditLogService auditLogService) {
        this.interventionRepository = interventionRepository;
        this.machineRepository = machineRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.auditLogService = auditLogService;
    }

    public List<Intervention> findByMachineId(Long machineId) {
        if (!machineRepository.existsById(machineId)) {
            throw new ResourceNotFoundException("Machine introuvable : " + machineId);
        }
        return interventionRepository.findByMachine_IdOrderByDateAsc(machineId);
    }

    public ResponseEntity<Intervention> save(Long machineId, Intervention intervention) {

        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new ResourceNotFoundException("Machine introuvable : " + machineId));

        intervention.setMachine(machine);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Utilisateur tech = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        intervention.setTechnicien(tech);

        if (intervention.getDate() == null) {
            intervention.setDate(LocalDate.now());
        }

        interventionRepository.save(intervention);

        auditLogService.log("CREATE", "Intervention","ID : " + intervention.getId() + "\nTechnicien : " + intervention.getTechnicien().getNom() + "\ndescription : " + intervention.getDescription() + "\nDuree : " + intervention.getDuree());

        return ResponseEntity.status(HttpStatus.CREATED).body(intervention);
    }
}