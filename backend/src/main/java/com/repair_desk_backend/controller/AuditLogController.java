package com.repair_desk_backend.controller;

import com.repair_desk_backend.dto.AuditHistoryResponse;
import com.repair_desk_backend.entity.AuditLog;
import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.service.AuditLogService;
import com.repair_desk_backend.service.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final UtilisateurService utilisateurService;


    public AuditLogController(AuditLogService auditLogService , UtilisateurService utilisateurService) {
        this.auditLogService = auditLogService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateur/{id}")
    public AuditHistoryResponse findByUtilisateur(@PathVariable Long id) {

        Utilisateur utilisateur = utilisateurService.findById(id);

        List<AuditLog> logs = auditLogService.findByUtilisateur(id);

        return new AuditHistoryResponse(
                utilisateur.getUsername(),
                logs
        );
    }
}