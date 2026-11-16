package com.repair_desk_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.repair_desk_backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Application user (an employee of the workshop who can log in).
 *
 * <p>This is the ONLY business entity the foundation ships with — it is required
 * for authentication. The intern adds the other entities (Client, FicheReception,
 * Machine, Intervention, Piece) on top of this foundation. See {@code Client} for a
 * worked example to copy.</p>
 */
@Entity
@Table(name = "utilisateur")
@Data                 // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor    // Lombok: required by JPA
@AllArgsConstructor   // Lombok: handy "all fields" constructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full display name, e.g. "Mohamed Ben Abdallah". */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /** Login name — must be unique across all users. */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "username est obligatoire")
    private String username;

    /** BCrypt-hashed password. NEVER store the plain text password here. */
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;

    /** Stored as text ("TECHNICIEN" / "ADMINISTRATEUR") instead of an ordinal number. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le role est obligatoire")
    private Role role;

    /** When false the user is disabled and cannot log in. */
    private Boolean actif = true;


}
