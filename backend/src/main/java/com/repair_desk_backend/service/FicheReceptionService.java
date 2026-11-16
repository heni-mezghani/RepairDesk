package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.*;
import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.ClientRepository;
import com.repair_desk_backend.repository.FicheReceptionRepository;
import com.repair_desk_backend.repository.UtilisateurRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FicheReceptionService {

    private final FicheReceptionRepository ficheReceptionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AuditLogService auditLogService;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public FicheReceptionService(FicheReceptionRepository ficheReceptionRepository, UtilisateurRepository utilisateurRepository, AuditLogService auditLogService, ClientService clientService,ClientRepository clientRepository) {
        this.ficheReceptionRepository = ficheReceptionRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.auditLogService = auditLogService;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    public List<FicheReception> findAll() {
        return ficheReceptionRepository.findAll();
    }

    public FicheReception findById(Long id) {
        return ficheReceptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiche introuvable : " + id));
    }

    public ResponseEntity<?> save(FicheReception fiche , Long client_id) {

        Client client = clientService.findById(client_id);

        fiche.setClient(client);

        if (fiche.getNumero() == null || fiche.getNumero().isEmpty()) {
            long nextId = ficheReceptionRepository.findMaxId() + 1;
            fiche.setNumero("F-" + LocalDate.now().getYear() + "-" + String.format("%03d", nextId));
        }

        if (fiche.getDateReception() == null) {
            fiche.setDateReception(LocalDate.now());
        }

        for (Machine machine : fiche.getMachines()) {
            machine.setFicheReception(fiche);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utilisateur tech = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        fiche.setTechnicienRecepteur(tech);

        ficheReceptionRepository.save(fiche);

        for (Machine machine : fiche.getMachines()) {
            auditLogService.log("CREATE", "Machine", "ID: " + machine.getId() + "\nMarque: " + machine.getMarque() + "\nType : " + machine.getType() + "\nModele : " + machine.getModele() + "\nnumero serie : " + machine.getNumeroSerie() + "\nProbleme : " + machine.getProblemeDeclare() + "\nDiagnostic : " + machine.getDiagnostic() + "\nCategorie : " + machine.getCategoriePanne() + "\nStatut : " + machine.getStatut() + "\nCoût Estimé : " + machine.getCoutEstime() + "\nCout Final : " + machine.getCoutFinal());
        }

        auditLogService.log("CREATE", "Fiche",  "ID : " + fiche.getId() + "\nnumero: " + fiche.getNumero() + "\nDate : " + fiche.getDateReception() + "\nClient : " + clientService.findById(fiche.getClient().getId()).getNom() + "\nTechnicien : " + fiche.getTechnicienRecepteur().getNom() + "\nMachines : " + fiche.getMachines().size());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public void delete(Long id) {
        FicheReception fiche = ficheReceptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiche introuvable"));

        auditLogService.log("DELETE", "Fiche", "ID : " + fiche.getId() + "\nnumero: " + fiche.getNumero() + "\nDate : " + fiche.getDateReception() + "\nClient : " + fiche.getClient().getNom() + "\nTechnicien : " + fiche.getTechnicienRecepteur().getNom() + "\nMachines : " + fiche.getMachines().size() );
        for (Machine machine : fiche.getMachines()) {
            auditLogService.log("DELETE", "Machine", "ID: " + machine.getId() + "\nMarque: " + machine.getMarque() + "\nType : " + machine.getType() + "\nModele : " + machine.getModele() + "\nnumero serie : " + machine.getNumeroSerie() + "\nProbleme : " + machine.getProblemeDeclare() + "\nDiagnostic : " + machine.getDiagnostic() + "\nCategorie : " + machine.getCategoriePanne() + "\nStatut : " + machine.getStatut() + "\nCoût Estimé : " + machine.getCoutEstime() + "\nCout Final : " + machine.getCoutFinal());
            if (machine.getInterventions().size() > 0){
                for (Intervention intervention : machine.getInterventions()) {
                    auditLogService.log("DELETE", "Intervention", "ID : " + intervention.getId() + "\nTechnicien : " + intervention.getTechnicien().getNom() + "\ndescription : " + intervention.getDescription() + "\nDuree : " + intervention.getDuree());
                    if (intervention.getPieces().size() > 0){
                        for (Piece piece : intervention.getPieces()) {
                            auditLogService.log("DELETE", "Piece", "ID : " + piece.getId() + "\nDesignation : " + piece.getDesignation() + "\nreference : " + piece.getReference() + "\nprix : " + piece.getPrixUnitaire() + "\nquantite : " + piece.getQuantite() );

                        }
                    }
                }
            }
        }
        ficheReceptionRepository.deleteById(id);
    }

    public List<FicheReception> findByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client introuvable : " + clientId);
        }
        return ficheReceptionRepository.findByClient_Id(clientId);
    }
}