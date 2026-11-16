package com.repair_desk_backend.dto;

import lombok.Data;

@Data
public class MachineSimpleDto {
    private String type;
    private String marque;
    private String modele;
    private String statut;

    public MachineSimpleDto(String type, String marque, String modele, String statut) {
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.statut = statut;
    }
}