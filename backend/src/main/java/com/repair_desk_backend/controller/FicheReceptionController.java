package com.repair_desk_backend.controller;

import com.repair_desk_backend.dto.FicheSimpleDto;
import com.repair_desk_backend.entity.FicheReception;
import com.repair_desk_backend.service.FicheReceptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiches")
public class FicheReceptionController {

    private final FicheReceptionService ficheReceptionService;

    public FicheReceptionController(FicheReceptionService ficheReceptionService) {
        this.ficheReceptionService = ficheReceptionService;
    }

    @GetMapping
    public List<FicheSimpleDto> findAll() {

        return ficheReceptionService.findAll()
                .stream()
                .map(fiche -> new FicheSimpleDto(
                        fiche.getId(),
                        fiche.getNumero(),
                        fiche.getDateReception(),
                        fiche.getClient().getNom(),
                        fiche.getTechnicienRecepteur().getNom(),
                        fiche.getMachines().size()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public FicheReception findById(@PathVariable Long id) {
        return ficheReceptionService.findById(id);
    }

    @PostMapping("/{client_id}")
    public ResponseEntity<?> create(@Valid @RequestBody FicheReception fiche , @PathVariable Long client_id) {
        return ficheReceptionService.save(fiche,client_id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ficheReceptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}