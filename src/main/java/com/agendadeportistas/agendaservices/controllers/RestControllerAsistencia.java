package com.agendadeportistas.agendaservices.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendadeportistas.agendaservices.services.AsistenciaService;
import com.agendadeportistas.agendaservices.shared.dto.AsistenciaDto;

@RestController
@CrossOrigin(origins = "http://localhost:4000")
@RequestMapping("/api/asistencias/")
public class RestControllerAsistencia {

    @Autowired
    AsistenciaService asistenciaService;

    @PostMapping(value = "guardar", headers = "Accept=application/json")
    public ResponseEntity<String> guardarAsistencias(@RequestBody List<AsistenciaDto> asistencias) {
        try {
            asistenciaService.guardarAsistencias(asistencias);
            return new ResponseEntity<>("Asistencias guardadas con éxito", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "listar/{fecha}", headers = "Accept=application/json")
    public List<AsistenciaDto> listarPorFecha(@PathVariable String fecha) {
        return asistenciaService.obtenerPorFecha(fecha);
    }
}
