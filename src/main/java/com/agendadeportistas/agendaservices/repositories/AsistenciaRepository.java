package com.agendadeportistas.agendaservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendadeportistas.agendaservices.entities.AsistenciaEntity;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Long> {
    List<AsistenciaEntity> findByFecha(String fecha);
}
