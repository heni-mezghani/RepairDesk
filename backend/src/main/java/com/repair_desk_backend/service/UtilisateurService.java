package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;


    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder , AuditLogService auditLogService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService=auditLogService;
    }

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
    }

    public ResponseEntity<?> create(Utilisateur utilisateur) {

        if (utilisateurRepository.existsByUsername(utilisateur.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ce nom d'utilisateur existe déjà"));
        }

        utilisateur.setActif(true);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        Utilisateur created = utilisateurRepository.save(utilisateur);

        auditLogService.log("CREATE", "Utilisateur", "ID : " + utilisateur.getId() + "\nNom : " + utilisateur.getNom() +   "\nUsername : " + utilisateur.getUsername() + "\nRole : " + utilisateur.getRole() + "\nStatut : " + utilisateur.getActif());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    public Utilisateur update(Long id, Utilisateur utilisateur) {
        Utilisateur existing = findById(id);

        auditLogService.log("UPDATE", "Utilisateur", "ID : " + existing.getId() +"\nNom : " + existing.getNom() + "\nUsername: " + existing.getUsername() + "\nRole : " + existing.getRole() +
                "\n→ " +  "\nNom : " + utilisateur.getNom() + "\nUsername: " + utilisateur.getUsername() + "\nRole : " + utilisateur.getRole());

        existing.setNom(utilisateur.getNom());
        existing.setUsername(utilisateur.getUsername());
        existing.setRole(utilisateur.getRole());

        return utilisateurRepository.save(existing);
    }

    public Utilisateur toggleActif(Long id) {
        Utilisateur utilisateur = findById(id);
        utilisateur.setActif(!utilisateur.getActif());

        auditLogService.log("ToggleActif", "Utilisateur",  "ID : " + utilisateur.getId() + "\nUsername: " + utilisateur.getUsername());

        return utilisateurRepository.save(utilisateur);
    }

}