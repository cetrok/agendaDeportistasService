package com.agendadeportistas.agendaservices.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agendadeportistas.agendaservices.entities.RecordatorioEntity;

public interface RecordatorioRepository extends JpaRepository<RecordatorioEntity, Long> {

}