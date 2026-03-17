package com.agendadeportistas.agendaservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.agendadeportistas.agendaservices.entities.AgendaEntity;

public interface AgendaRepository extends JpaRepository<AgendaEntity, Long> {

    @Transactional
    void deleteByGrupo_IdGrupo(Long idGrupo);
}
