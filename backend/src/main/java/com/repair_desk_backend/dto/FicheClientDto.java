package com.repair_desk_backend.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class FicheClientDto {

    private String numero;
    private LocalDate dateReception;
    private String technicien;
    private Long id;
    private List<MachineSimpleDto> machines;

    public FicheClientDto(Long id,
                          String numero,
                          LocalDate dateReception,
                          String technicien,
                          List<MachineSimpleDto> machines) {
        this.id=id;
        this.numero = numero;
        this.dateReception = dateReception;
        this.technicien = technicien;
        this.machines = machines;
    }
}