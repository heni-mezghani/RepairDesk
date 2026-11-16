package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.Intervention;
import com.repair_desk_backend.entity.Piece;
import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.InterventionRepository;
import com.repair_desk_backend.repository.PieceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PieceService {

    private final PieceRepository pieceRepository;
    private final InterventionRepository interventionRepository;
    private final AuditLogService auditLogService;

    public PieceService(PieceRepository pieceRepository,
                        InterventionRepository interventionRepository,
                        AuditLogService auditLogService) {
        this.pieceRepository = pieceRepository;
        this.interventionRepository = interventionRepository;
        this.auditLogService = auditLogService;
    }

    public Piece findById(Long id) {
        return pieceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Piece introuvable : " + id));
    }

    public ResponseEntity<Piece> save(Long interventionId, Piece piece) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention introuvable"));
        piece.setIntervention(intervention);
        pieceRepository.save(piece);

        auditLogService.log("CREATE", "Piece",  "Id: " + piece.getId() + "\nDesignation : " + piece.getDesignation()  +  "\nreference : " + piece.getReference() + "\nprix : " + piece.getPrixUnitaire()  + "\nquantite : " + piece.getQuantite());

        return ResponseEntity.status(HttpStatus.CREATED).body(piece);
    }

    public void delete(Long id) {
        Piece existing = findById(id);
        auditLogService.log("DELETE", "Piece", "ID : " + id + "\nDesignation : " + existing.getDesignation() + "\nreference : " + existing.getReference() + "\nprix : " + existing.getPrixUnitaire() + "\nquantite : " + existing.getQuantite() );
        pieceRepository.deleteById(id);
    }
}