package com.repair_desk_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "intervention")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @NotBlank(message = "description est obligatoire")
    private String description;

    @NotNull(message = "duree est obligatoire")
    private Integer duree;

    @ManyToOne
    @JoinColumn(name = "technicien_id")
    @JsonIgnoreProperties({"id","username","role","actif"})
    private Utilisateur technicien;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    @JsonIgnore
    private Machine machine;

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.REMOVE)
    private List<Piece> pieces = new ArrayList<>();
}