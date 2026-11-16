package com.repair_desk_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FicheReception")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FicheReception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numero;

    private LocalDate dateReception;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"id","type"})
    private Client client;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    @JsonIgnoreProperties({"id","motDePasse", "actif", "role", "username"})
    private Utilisateur technicienRecepteur;

    @OneToMany(mappedBy = "ficheReception", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JsonIgnoreProperties({"id","ficheReception"})
    @NotEmpty(message = "La liste des machines ne peut pas être vide")
    @Valid
    private List<Machine> machines = new ArrayList<>();
}
