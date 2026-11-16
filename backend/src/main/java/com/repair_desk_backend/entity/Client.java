package com.repair_desk_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.repair_desk_backend.enums.TypeClient;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * EXAMPLE TEMPLATE — intern: replicate this pattern for other entities
 * (FicheReception, Machine, Intervention, Piece).
 *
 * <p>A customer of the workshop. This is the ENTITY layer of the worked example.
 * Notice:</p>
 * <ul>
 *   <li>Lombok ({@code @Data}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor})
 *       removes the getter/setter/constructor boilerplate.</li>
 *   <li>JPA annotations ({@code @Entity}, {@code @Id}, {@code @GeneratedValue}) map the
 *       class to a database table.</li>
 *   <li>Bean-validation annotations ({@code @NotBlank}, {@code @Email}) are checked when
 *       a controller method marks the body with {@code @Valid}. There is NO separate DTO —
 *       per the project rules, validation lives directly on the entity.</li>
 * </ul>
 */
@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le telephone est obligatoire")
    @Pattern(regexp = "^[0-9]+$", message = "Le téléphone doit contenir uniquement des chiffres")
    private String telephone;

    @NotBlank(message = "L'addresse email est obligatoire")
    @Email(message = "L'adresse e-mail n'est pas valide")
    private String email;

    @NotBlank(message = "L'addresse est obligatoire")
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le type est obligatoire")
    private TypeClient type;

    @OneToMany(mappedBy = "client", cascade = { CascadeType.REMOVE })
    @JsonIgnore
    private List<FicheReception> fiche = new ArrayList<>();


    // TODO (intern): add the fields you need, plus relationships to other entities, e.g.
    //   @OneToMany(mappedBy = "client")
    //   private List<FicheReception> fiches;
}
