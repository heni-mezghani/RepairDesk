package com.repair_desk_backend.controller;

import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<Utilisateur> findAll() {
        return utilisateurService.findAll();
    }

    @GetMapping("/{id}")
    public Utilisateur findById(@PathVariable Long id) {
        return utilisateurService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Utilisateur utilisateur) {
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("fieldErrors", Map.of("motDePasse", "Le mot de passe est obligatoire"))
            );
        }
        return utilisateurService.create(utilisateur);
    }

    @PatchMapping("/{id}")
    public Utilisateur update(@PathVariable Long id,@Valid @RequestBody Utilisateur utilisateur) {
        return utilisateurService.update(id, utilisateur);
    }

    @PatchMapping("/{id}/toggle-actif")
    public Utilisateur toggleActif(@PathVariable Long id) {
        return utilisateurService.toggleActif(id);
    }

}