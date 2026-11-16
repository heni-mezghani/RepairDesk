package com.repair_desk_backend.service;

import com.repair_desk_backend.entity.*;

import com.repair_desk_backend.exception.ResourceNotFoundException;
import com.repair_desk_backend.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EXAMPLE TEMPLATE — intern: replicate this pattern for other entities.
 *
 * <p>The SERVICE layer of the worked example. This is where business logic lives. The
 * controller stays thin and delegates here; the repository only talks to the database.</p>
 *
 * <p>Key things to copy:</p>
 * <ul>
 *   <li>It works DIRECTLY with the {@link Client} entity — no DTOs (project rule).</li>
 *   <li>"Not found" is signalled by throwing {@link ResourceNotFoundException}, which the
 *       global handler turns into a clean 404.</li>
 *   <li>For an update we load the existing row first, then copy the changed fields onto it,
 *       so the id and untouched fields are preserved.</li>
 * </ul>
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AuditLogService auditLogService;


    public ClientService(ClientRepository clientRepository , AuditLogService auditLogService) {
        this.clientRepository = clientRepository;
        this.auditLogService = auditLogService;
    }

    /** List every client. */
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    /** Get one client by id, or throw 404 if it does not exist. */
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable : " + id));
    }

    /** Create a new client (id must be null so the database generates it). */
    public ResponseEntity<Void> create(Client client) {
        client.setId(null); // ensure we INSERT a new row rather than overwrite an existing one
        clientRepository.save(client);
        auditLogService.log("CREATE", "Client", "ID: " + client.getId() + "\nNom: " + client.getNom() + "\nTelephone : " + client.getTelephone() + "\nEmail : " + client.getEmail() + "\nAdresse : " + client.getAdresse() + "\nType : " + client.getType());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** Update an existing client. Throws 404 if the id does not exist. */
    public Client update(Long id, Client changes) {
        Client existing = findById(id); // reuses the 404 logic above

        auditLogService.log("UPDATE", "Client", "ID: " + id + "\nNom: " + existing.getNom() + "\nTelephone : " + existing.getTelephone() + "\nEmail : " + existing.getEmail() + "\nAdresse : " + existing.getAdresse() + "\nType : " + existing.getType() +
                "\n→" + "\nNom: " + changes.getNom() + "\nTelephone : " + changes.getTelephone() + "\nEmail : " + changes.getEmail() + "\nAdresse : " + changes.getAdresse() + "\nType : " + changes.getType() );

        // Copy the editable fields from the incoming object onto the managed entity.
        existing.setNom(changes.getNom());
        existing.setTelephone(changes.getTelephone());
        existing.setEmail(changes.getEmail());
        existing.setAdresse(changes.getAdresse());
        existing.setType(changes.getType());

        return clientRepository.save(existing);
    }

    /** Delete a client by id. Throws 404 if it does not exist. */
    public void delete(Long id) {
        Client existing = findById(id);
        auditLogService.log("DELETE", "Client", "ID: " + id + "\n Nom : " + existing.getNom() + "\nTelephone : " + existing.getTelephone() + "\nEmail : " + existing.getEmail() + "\nAdresse : " + existing.getAdresse() + "\nType : " + existing.getType());
        if (existing.getFiche().size() > 0){
            for (FicheReception fiche : existing.getFiche()) {
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
            }
        }
        clientRepository.delete(existing);
    }
}
