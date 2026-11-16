package com.repair_desk_backend.controller;

import com.repair_desk_backend.dto.FicheDuJourDto;
import com.repair_desk_backend.dto.MachineTermineeDto;
import com.repair_desk_backend.repository.FicheReceptionRepository;
import com.repair_desk_backend.repository.InterventionRepository;
import com.repair_desk_backend.repository.MachineRepository;
import com.repair_desk_backend.entity.Machine;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final MachineRepository machineRepository;
    private final FicheReceptionRepository ficheReceptionRepository;
    private final InterventionRepository interventionRepository;

    public DashboardController(MachineRepository machineRepository,
                               FicheReceptionRepository ficheReceptionRepository,
                               InterventionRepository interventionRepository) {
        this.machineRepository = machineRepository;
        this.ficheReceptionRepository = ficheReceptionRepository;
        this.interventionRepository = interventionRepository;
    }

    @GetMapping("/machines-par-statut")
    public Map<String, Long> machinesParStatut() {
        List<Machine> machines = machineRepository.findAll();
        return machines.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getStatut().name(),
                        Collectors.counting()
                ));
    }

    @GetMapping("/fiches-du-jour")
    public List<FicheDuJourDto> fichesDuJour() {

        return ficheReceptionRepository.findByDateReception(LocalDate.now())
                .stream()
                .map(fiche -> {
                    FicheDuJourDto dto = new FicheDuJourDto();

                    dto.setId(fiche.getId());
                    dto.setNumero(fiche.getNumero());
                    dto.setClient(fiche.getClient().getNom());
                    dto.setTechnicien(fiche.getTechnicienRecepteur().getNom());
                    dto.setNombreMachines(fiche.getMachines().size());

                    return dto;
                })
                .toList();
    }

    @GetMapping("/machines-terminees")
    public List<MachineTermineeDto> machinesTerminees() {
        return machineRepository.findAll()
                .stream().filter(m -> m.getStatut().name().equals("TERMINE"))
                .map(m -> {
                    MachineTermineeDto dto = new MachineTermineeDto();

                    dto.setId(m.getId());
                    dto.setType(m.getType());
                    dto.setMarque(m.getMarque());
                    dto.setModele(m.getModele());
                    dto.setNumeroSerie(m.getNumeroSerie());

                    return dto;
                })
                .toList();
    }

    @GetMapping("/pannes-par-categorie")
    public Map<String, Long> pannesParCategorie() {
        List<Machine> machines = machineRepository.findAll();
        return machines.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getCategoriePanne().name(),
                        Collectors.counting()
                ));
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @GetMapping("/interventions-par-technicien")
    public Map<String, Long> interventionsParTechnicien() {
        return interventionRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        i -> i.getTechnicien().getNom(),
                        Collectors.counting()
                ));
    }
}