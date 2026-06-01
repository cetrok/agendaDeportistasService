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

import com.agendadeportistas.agendaservices.services.PaqueteClasesService;
import com.agendadeportistas.agendaservices.shared.dto.PaqueteClasesDto;

@RestController
@CrossOrigin(origins = "http://localhost:4000")
@RequestMapping("/api/paquetes/")
public class RestControllerPaquetes {

    @Autowired
    PaqueteClasesService paqueteClasesService;

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<PaqueteClasesDto> crearPaquete(@RequestBody PaqueteClasesDto paqueteDto) {
        PaqueteClasesDto creado = paqueteClasesService.crearPaquete(paqueteDto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping(value = "listar/{deportistaId}", headers = "Accept=application/json")
    public List<PaqueteClasesDto> listarPorDeportista(@PathVariable String deportistaId) {
        return paqueteClasesService.obtenerPaquetesPorDeportista(deportistaId);
    }

    @GetMapping(value = "agotados", headers = "Accept=application/json")
    public List<PaqueteClasesDto> listarAgotados() {
        return paqueteClasesService.obtenerDeportistasConPaqueteAgotado();
    }
}
