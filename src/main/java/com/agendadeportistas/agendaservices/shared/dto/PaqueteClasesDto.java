package com.agendadeportistas.agendaservices.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteClasesDto {
    private Long idPaquete;
    private DeportistaLightDto deportista;
    private int totalClases;
    private int clasesRestantes;
    private String fechaPago;
    private boolean activo;
}
