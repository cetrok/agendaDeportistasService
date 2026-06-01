package com.agendadeportistas.agendaservices.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendadeportistas.agendaservices.entities.PaqueteClasesEntity;

public interface PaqueteClasesRepository extends JpaRepository<PaqueteClasesEntity, Long> {

    List<PaqueteClasesEntity> findByDeportista_IdOrderByIdPaqueteDesc(String deportistaId);

    Optional<PaqueteClasesEntity> findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc(String deportistaId);

    List<PaqueteClasesEntity> findByActivo(boolean activo);
}
