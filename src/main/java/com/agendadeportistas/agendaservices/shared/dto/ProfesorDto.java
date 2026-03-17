package com.agendadeportistas.agendaservices.shared.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProfesorDto {
    private String id;
    private String nombre;
    private String tipoId;
    private String numeroCelular;
    private String direccion;
    private String eps;
    private String arl;
    private String correoElectronico;
    private String nombreContacto;
    private String numeroContacto;
    private List<DisponibilidadDto> disponibilidades;
    private Boolean estado;
}
