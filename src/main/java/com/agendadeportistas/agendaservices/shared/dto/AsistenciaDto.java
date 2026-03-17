package com.agendadeportistas.agendaservices.shared.dto;

import lombok.Data;

@Data
public class AsistenciaDto {
    Long idAsistencia;
    AgendaDto agenda;
    String fecha;
    boolean asistio;
}
