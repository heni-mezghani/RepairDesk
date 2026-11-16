package com.repair_desk_backend.controller;

import com.repair_desk_backend.dto.FicheClientDto;
import com.repair_desk_backend.dto.MachineSimpleDto;
import com.repair_desk_backend.entity.Client;
import com.repair_desk_backend.service.ClientService;
import com.repair_desk_backend.service.FicheReceptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EXAMPLE TEMPLATE — intern: replicate this pattern for other entities.
 *
 * <p>The CONTROLLER layer of the worked example: a standard REST CRUD controller.
 * It is intentionally thin — it just maps HTTP to {@link ClientService} calls and
 * returns entities directly (no DTOs, per the project rules).</p>
 *
 * <p>This endpoint is NOT under {@code /api/auth/**}, so {@code SecurityConfig} already
 * requires a valid JWT to reach it — no extra work needed for basic protection.</p>
 *
 * <p>Endpoints:</p>
 * <pre>
 *   GET    /api/clients        -> list all
 *   GET    /api/clients/{id}   -> get one (404 if missing)
 *   POST   /api/clients        -> create (201)
 *   PUT    /api/clients/{id}   -> update (404 if missing)
 *   DELETE /api/clients/{id}   -> delete (204, 404 if missing)
 * </pre>
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final FicheReceptionService ficheReceptionService;



    public ClientController(ClientService clientService,FicheReceptionService ficheReceptionService) {
        this.clientService = clientService;
        this.ficheReceptionService = ficheReceptionService;
    }

    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    /** {@code @Valid} triggers the entity's validation annotations; failures become a 400. */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Client client) {
        return clientService.create(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @Valid @RequestBody Client client) {
        return clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}/fiches")
    public List<FicheClientDto> getFichesByClient(@PathVariable Long id) {

        return ficheReceptionService.findByClientId(id)
                .stream()
                .map(fiche -> new FicheClientDto(
                        fiche.getId(),
                        fiche.getNumero(),
                        fiche.getDateReception(),
                        fiche.getTechnicienRecepteur().getNom(),
                        fiche.getMachines().stream()
                                .map(m -> new MachineSimpleDto(
                                        m.getType(),
                                        m.getMarque(),
                                        m.getModele(),
                                        m.getStatut().name()
                                ))
                                .toList()
                ))
                .toList();
    }


    // ----------------------------------------------------------------------------------
    // TODO (intern): create the same four layers (entity + repository + service +
    // controller) for each remaining module, copying this Client example:
    //   - FicheReception  (the intake form: links a Client to the device(s) brought in)
    //   - Machine         (a device under repair)
    //   - Intervention    (a repair action performed on a Machine)
    //   - Piece           (a spare part used during an Intervention)
    // Remember: work directly with entities, NO DTOs (login is the only exception).
    // To restrict an endpoint to admins, add @PreAuthorize("hasRole('ADMINISTRATEUR')").
    // ----------------------------------------------------------------------------------
}
