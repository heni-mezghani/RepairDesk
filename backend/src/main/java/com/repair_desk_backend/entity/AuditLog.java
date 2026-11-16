package com.repair_desk_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String entite;

    @Column(columnDefinition = "TEXT")
    private String details;



    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "utiliateur_id")
    @JsonIgnore
    private Utilisateur technicien;
}