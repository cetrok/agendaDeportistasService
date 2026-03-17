package com.agendadeportistas.agendaservices.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendadeportistas.agendaservices.entities.AgendaEntity;
import com.agendadeportistas.agendaservices.entities.AsistenciaEntity;
import com.agendadeportistas.agendaservices.repositories.AgendaRepository;
import com.agendadeportistas.agendaservices.repositories.AsistenciaRepository;
import com.agendadeportistas.agendaservices.shared.dto.AgendaDto;
import com.agendadeportistas.agendaservices.shared.dto.AsistenciaDto;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public void guardarAsistencias(List<AsistenciaDto> asistencias) {
        for (AsistenciaDto dto : asistencias) {
            AgendaEntity agenda = agendaRepository.findById(dto.getAgenda().getIdAgenda())
                    .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

            AsistenciaEntity entity = new AsistenciaEntity();
            entity.setAgenda(agenda);
            entity.setFecha(dto.getFecha());
            entity.setAsistio(dto.isAsistio());

            asistenciaRepository.save(entity);
        }
    }

    public List<AsistenciaDto> obtenerPorFecha(String fecha) {
        return asistenciaRepository.findByFecha(fecha).stream()
                .map(entity -> {
                    AsistenciaDto dto = new AsistenciaDto();
                    dto.setIdAsistencia(entity.getIdAsistencia());
                    dto.setFecha(entity.getFecha());
                    dto.setAsistio(entity.isAsistio());
                    AgendaDto agendaDto = new AgendaDto();
                    agendaDto.setIdAgenda(entity.getAgenda().getIdAgenda());
                    dto.setAgenda(agendaDto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
