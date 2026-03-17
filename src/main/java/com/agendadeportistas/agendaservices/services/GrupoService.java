package com.agendadeportistas.agendaservices.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendadeportistas.agendaservices.entities.CursoEntity;
import com.agendadeportistas.agendaservices.entities.GrupoEntity;
import com.agendadeportistas.agendaservices.entities.ProfesorEntity;
import com.agendadeportistas.agendaservices.entities.UbicacionEntity;
import com.agendadeportistas.agendaservices.repositories.AgendaRepository;
import com.agendadeportistas.agendaservices.repositories.CursoRepository;
import com.agendadeportistas.agendaservices.repositories.GrupoRepository;
import com.agendadeportistas.agendaservices.repositories.ProfesorRepository;
import com.agendadeportistas.agendaservices.repositories.UbicacionRepository;
import com.agendadeportistas.agendaservices.shared.dto.CursoDto;
import com.agendadeportistas.agendaservices.shared.dto.GrupoDto;
import com.agendadeportistas.agendaservices.shared.dto.ProfesorDto;
import com.agendadeportistas.agendaservices.shared.dto.UbicacionDto;

@Service
public class GrupoService {
    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public void crearGrupo(GrupoDto grupoDto) {
        GrupoEntity grupo = mapearToEntity(grupoDto);

        grupoRepository.save(grupo);

        Optional<CursoEntity> curso = cursoRepository.findById(grupoDto.getCurso().getIdCurso());

        if (curso.isPresent()) {
            curso.get().getGrupos().add(grupo);
            cursoRepository.save(curso.get());
        }

        Optional<ProfesorEntity> profesor = profesorRepository.findById(grupoDto.getProfesor().getId());

        if (profesor.isPresent()) {
            profesor.get().getGrupos().add(grupo);
            profesorRepository.save(profesor.get());
        }

        Optional<UbicacionEntity> ubicacion = ubicacionRepository.findById(grupoDto.getUbicacion().getId());

        if (ubicacion.isPresent()) {
            ubicacion.get().getGrupos().add(grupo);
            ubicacionRepository.save(ubicacion.get());
        }
    }

    @Transactional
    public void actualizarGrupo(GrupoDto grupoDto) {
        GrupoEntity grupo = grupoRepository.findById(grupoDto.getIdGrupo())
                .orElseThrow(() -> new RuntimeException("Grupo not found"));

        grupo = updateEntity(grupoDto, grupo);
        grupoRepository.save(grupo);
    }

    public GrupoEntity mapearToEntity(GrupoDto grupo) {
        GrupoEntity grupoDto = new GrupoEntity();

        grupoDto.setDia(grupo.getDia());
        grupoDto.setHoraInicio(grupo.getHoraInicio());
        grupoDto.setHoraFin(grupo.getHoraFin());
        grupoDto.setCupos(grupo.getCupos());

        grupoDto.setUbicacion(ubicacionRepository.findById(grupo.getUbicacion().getId()).get());
        grupoDto.setProfesor(profesorRepository.findById(grupo.getProfesor().getId()).get());
        grupoDto.setCurso(cursoRepository.findById(grupo.getCurso().getIdCurso()).get());

        return grupoDto;
    }

    public GrupoEntity updateEntity(GrupoDto grupo, GrupoEntity grupoDto) {
        grupoDto.setDia(grupo.getDia());
        grupoDto.setHoraInicio(grupo.getHoraInicio());
        grupoDto.setHoraFin(grupo.getHoraFin());
        grupoDto.setCupos(grupo.getCupos());

        grupoDto.setUbicacion(ubicacionRepository.findById(grupo.getUbicacion().getId()).get());
        grupoDto.setProfesor(profesorRepository.findById(grupo.getProfesor().getId()).get());
        grupoDto.setCurso(cursoRepository.findById(grupo.getCurso().getIdCurso()).get());

        return grupoDto;
    }

    public GrupoDto mapearToDto(GrupoEntity grupoEntity) {
        GrupoDto grupoDto = new GrupoDto();

        grupoDto.setDia(grupoEntity.getDia());
        grupoDto.setHoraInicio(grupoEntity.getHoraInicio());
        grupoDto.setHoraFin(grupoEntity.getHoraFin());
        grupoDto.setCupos(grupoEntity.getCupos());

        // se obtienen los datos de la ubicacion del grupo
        grupoDto.setIdGrupo(grupoEntity.getIdGrupo());
        grupoDto.setUbicacion(ubicacionRepository.findById(grupoEntity.getUbicacion().getId())
                .map(ubicacion -> {
                    UbicacionDto ubicacionDto = new UbicacionDto();
                    ubicacionDto.setId(ubicacion.getId());
                    ubicacionDto.setNombre(ubicacion.getNombre());
                    ubicacionDto.setDireccion(ubicacion.getDireccion());
                    return ubicacionDto;
                })
                .orElseThrow(() -> new RuntimeException("Ubicacion not found")));
        grupoDto.setProfesor(profesorRepository.findById(grupoEntity.getProfesor().getId())
                .map(profesor -> {
                    ProfesorDto profesorDto = new ProfesorDto();
                    profesorDto.setId(profesor.getId());
                    profesorDto.setNombre(profesor.getNombre());
                    return profesorDto;
                })
                .orElseThrow(() -> new RuntimeException("Profesor not found")));
        grupoDto.setCurso(cursoRepository.findById(grupoEntity.getCurso().getIdCurso())
                .map(curso -> {
                    CursoDto cursoDto = new CursoDto();
                    cursoDto.setIdCurso(curso.getIdCurso());
                    cursoDto.setNombre(curso.getNombre());
                    cursoDto.setColor(curso.getColor());

                    return cursoDto;
                })
                .orElseThrow(() -> new RuntimeException("Curso not found")));

        return grupoDto;
    }

    public List<GrupoDto> findAll() {
        // Se mapean los grupos de entity a DTO
        return grupoRepository.findAll()
                .stream()
                .map(this::mapearToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarGrupo(long idGrupo) {
        agendaRepository.deleteByGrupo_IdGrupo(idGrupo);
        grupoRepository.deleteById(idGrupo);
    }

}
