package com.agendadeportistas.agendaservices.repositories;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendadeportistas.agendaservices.entities.AsistenciaEntity;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Long> {
    List<AsistenciaEntity> findByFecha(String fecha);
    Optional<AsistenciaEntity> findByAgenda_IdAgendaAndFecha(Long idAgenda, String fecha);
}
