package com.repair_desk_backend.dto;

import lombok.Data;

@Data
public class FicheDuJourDto {

    private Long id;
    private String numero;
    private String client;
    private String technicien;
    private int nombreMachines;

}