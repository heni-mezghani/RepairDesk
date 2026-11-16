package com.repair_desk_backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FicheSimpleDto {

    private String numero;
    private LocalDate dateReception;
    private String client;
    private String technicien;
    private int nombreMachines;
    private Long id;

    public FicheSimpleDto(Long id,
                          String numero,
                          LocalDate dateReception,
                          String client,
                          String technicien,
                          int nombreMachines
                          ) {
        this.numero = numero;
        this.dateReception = dateReception;
        this.client = client;
        this.technicien = technicien;
        this.nombreMachines = nombreMachines;
        this.id = id;
    }

}