package com.repair_desk_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "piece")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "designation est obligatoire")
    private String designation;

    @NotBlank(message = "reference est obligatoire")
    private String reference;

    @NotNull(message = "prixUnitaire est obligatoire")
    private Double prixUnitaire;

    @NotNull(message = "quantite est obligatoire")
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    @JsonIgnore
    private Intervention intervention;
}