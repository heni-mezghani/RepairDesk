package com.repair_desk_backend.controller;

import com.repair_desk_backend.entity.Piece;
import com.repair_desk_backend.service.PieceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/interventions/{interventionId}/pieces")
public class PieceController {

    private final PieceService pieceService;

    public PieceController(PieceService pieceService) {
        this.pieceService = pieceService;
    }

    @PostMapping
    public ResponseEntity<Piece> create(@PathVariable Long interventionId,
                        @Valid @RequestBody Piece piece) {
        return pieceService.save(interventionId, piece);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable Long id ) {
        pieceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}