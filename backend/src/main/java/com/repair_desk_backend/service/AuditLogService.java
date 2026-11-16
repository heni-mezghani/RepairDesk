package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.AuditLog;
import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.repository.AuditLogRepository;
import com.repair_desk_backend.repository.UtilisateurRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UtilisateurRepository utilisateurRepository;


    public AuditLogService(AuditLogRepository auditLogRepository,UtilisateurRepository utilisateurRepository) {
        this.auditLogRepository = auditLogRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public void log(String action, String entite, String details) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Utilisateur utilisateur = utilisateurRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntite(entite);
        log.setDetails(details);
        log.setDate(LocalDateTime.now());
        log.setTechnicien(utilisateur);

        auditLogRepository.save(log);
    }

    public List<AuditLog> findByUtilisateur(Long utilisateurId) {
        return auditLogRepository.findByTechnicien_IdOrderByDateDesc(utilisateurId);
    }
}