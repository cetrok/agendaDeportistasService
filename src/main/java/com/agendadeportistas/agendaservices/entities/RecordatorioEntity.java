package com.agendadeportistas.agendaservices.entities;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recordatorio")
public class RecordatorioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recordatorio")
    long idRecordatorio;

    String titulo;
    String contenido;
    Date creado;
    Date fechaRecordatorio;
    int diasRecordatorio;
    Boolean activo;
}
