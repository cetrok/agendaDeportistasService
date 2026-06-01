package com.agendadeportistas.agendaservices.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paquete_clases")
public class PaqueteClasesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Long idPaquete;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deportista_id")
    private DeportistaEntity deportista;

    @Column(name = "total_clases")
    private int totalClases;

    @Column(name = "clases_restantes")
    private int clasesRestantes;

    @Column(name = "fecha_pago")
    private String fechaPago;

    @Column(name = "activo")
    private boolean activo;
}
