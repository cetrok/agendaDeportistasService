package com.agendadeportistas.agendaservices.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendadeportistas.agendaservices.entities.DeportistaEntity;
import com.agendadeportistas.agendaservices.entities.PaqueteClasesEntity;
import com.agendadeportistas.agendaservices.repositories.DeportistaRepository;
import com.agendadeportistas.agendaservices.repositories.PaqueteClasesRepository;
import com.agendadeportistas.agendaservices.shared.dto.DeportistaLightDto;
import com.agendadeportistas.agendaservices.shared.dto.PaqueteClasesDto;

@Service
public class PaqueteClasesService {

    @Autowired
    private PaqueteClasesRepository paqueteClasesRepository;

    @Autowired
    private DeportistaRepository deportistaRepository;

    @Transactional
    public PaqueteClasesDto crearPaquete(PaqueteClasesDto dto) {
        DeportistaEntity deportista = deportistaRepository.findById(dto.getDeportista().getId())
                .orElseThrow(() -> new RuntimeException("Deportista no encontrado"));

        PaqueteClasesEntity entity = new PaqueteClasesEntity();
        entity.setDeportista(deportista);
        entity.setTotalClases(dto.getTotalClases());
        entity.setClasesRestantes(dto.getTotalClases());
        entity.setFechaPago(dto.getFechaPago());
        entity.setActivo(true);

        PaqueteClasesEntity saved = paqueteClasesRepository.save(entity);
        return toDto(saved);
    }

    public List<PaqueteClasesDto> obtenerPaquetesPorDeportista(String deportistaId) {
        return paqueteClasesRepository.findByDeportista_IdOrderByIdPaqueteDesc(deportistaId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void descontarClase(String deportistaId) {
        Optional<PaqueteClasesEntity> paqueteOpt =
                paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc(deportistaId);

        if (paqueteOpt.isEmpty()) return;

        PaqueteClasesEntity paquete = paqueteOpt.get();
        int restantes = paquete.getClasesRestantes() - 1;
        paquete.setClasesRestantes(Math.max(restantes, 0));

        if (paquete.getClasesRestantes() == 0) {
            paquete.setActivo(false);
        }

        paqueteClasesRepository.save(paquete);
    }

    public List<PaqueteClasesDto> obtenerDeportistasConPaqueteAgotado() {
        Set<String> conPaqueteActivo = paqueteClasesRepository.findByActivo(true)
                .stream()
                .map(p -> p.getDeportista().getId())
                .collect(Collectors.toSet());

        Set<String> conAlgunPaquete = paqueteClasesRepository.findAll()
                .stream()
                .map(p -> p.getDeportista().getId())
                .collect(Collectors.toSet());

        // Deportistas con paquete agotado (tuvieron paquete pero ya no tienen activo)
        List<PaqueteClasesDto> agotados = paqueteClasesRepository.findByActivo(false).stream()
                .filter(p -> !conPaqueteActivo.contains(p.getDeportista().getId()))
                .collect(Collectors.toMap(
                        p -> p.getDeportista().getId(),
                        p -> p,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        // Deportistas sin ningún paquete registrado
        List<PaqueteClasesDto> sinPaquete = deportistaRepository.findAll().stream()
                .filter(d -> !conAlgunPaquete.contains(d.getId()))
                .map(d -> {
                    PaqueteClasesDto dto = new PaqueteClasesDto();
                    DeportistaLightDto deportistaDto = new DeportistaLightDto();
                    deportistaDto.setId(d.getId());
                    deportistaDto.setNombre(d.getNombre());
                    dto.setDeportista(deportistaDto);
                    dto.setIdPaquete(null);
                    dto.setTotalClases(0);
                    dto.setClasesRestantes(0);
                    dto.setActivo(false);
                    return dto;
                })
                .collect(Collectors.toList());

        List<PaqueteClasesDto> resultado = new ArrayList<>(agotados);
        resultado.addAll(sinPaquete);
        return resultado;
    }

    public boolean tieneActivoPaquete(String deportistaId) {
        return paqueteClasesRepository
                .findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc(deportistaId)
                .isPresent();
    }

    public List<String> obtenerIdsSinPaqueteActivo() {
        Set<String> conPaqueteActivo = paqueteClasesRepository.findByActivo(true)
                .stream()
                .map(p -> p.getDeportista().getId())
                .collect(Collectors.toSet());

        return deportistaRepository.findAll().stream()
                .map(DeportistaEntity::getId)
                .filter(id -> !conPaqueteActivo.contains(id))
                .collect(Collectors.toList());
    }

    private PaqueteClasesDto toDto(PaqueteClasesEntity entity) {
        PaqueteClasesDto dto = new PaqueteClasesDto();
        dto.setIdPaquete(entity.getIdPaquete());
        dto.setTotalClases(entity.getTotalClases());
        dto.setClasesRestantes(entity.getClasesRestantes());
        dto.setFechaPago(entity.getFechaPago());
        dto.setActivo(entity.isActivo());

        DeportistaLightDto deportistaDto = new DeportistaLightDto();
        deportistaDto.setId(entity.getDeportista().getId());
        deportistaDto.setNombre(entity.getDeportista().getNombre());
        dto.setDeportista(deportistaDto);

        return dto;
    }
}
