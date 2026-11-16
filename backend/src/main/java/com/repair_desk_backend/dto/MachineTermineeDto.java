package com.repair_desk_backend.dto;


import lombok.Data;

@Data
public class MachineTermineeDto {

    private Long id;
    private String type;
    private String marque;
    private String modele;
    private String numeroSerie;


}