package com.agendadeportistas.agendaservices.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profesor")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ProfesorEntity.class)
public class ProfesorEntity {
    @Id
    @Column(name = "id_profesor")
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
    private Boolean estado;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisponibilidadProfesorEntity> disponibilidades;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GrupoEntity> grupos;
}
