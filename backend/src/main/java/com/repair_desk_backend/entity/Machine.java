package com.repair_desk_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.repair_desk_backend.enums.CategoriePanne;
import com.repair_desk_backend.enums.Statut;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Machine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le type est obligatoire")
    private String type;

    @NotBlank(message = "Le marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modele est obligatoire")
    private String modele;

    private String numeroSerie;

    @PrePersist
    @PreUpdate
    public void setDefaultNumeroSerie() {
        if (numeroSerie == null || numeroSerie.isBlank()) {
            numeroSerie = "---";
        }
    }

    @NotBlank(message = "Le problemeDeclare est obligatoire")
    private String problemeDeclare;

    @NotBlank(message = "Le diagnostic est obligatoire")
    private String diagnostic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le categorie de panne est obligatoire")
    private CategoriePanne categoriePanne;

    @NotNull(message = "Le coût Estime est obligatoire")
    private Double coutEstime;

    @NotNull(message = "Le coût final est obligatoire")
    private Double coutFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le statut est obligatoire")
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "fiche_reception_id")
    @JsonIgnore
    private FicheReception ficheReception;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Intervention> interventions = new ArrayList<>();
}
