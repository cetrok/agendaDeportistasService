package com.agendadeportistas.agendaservices.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendadeportistas.agendaservices.shared.dto.GrupoDto;
import com.agendadeportistas.agendaservices.services.GrupoService;

@RestController
@RequestMapping("/api/grupos/")
public class RestControllerGrupo {
    @Autowired
    GrupoService grupoService;

    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<String> crearGrupo(@RequestBody GrupoDto grupoRq) {
        // Crear el Grupo
        grupoService.crearGrupo(grupoRq);

        return new ResponseEntity<>("Grupo creado con exito", HttpStatus.CREATED);
    }

    // Petición para actualizar un Grupo
    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "actualizar", headers = "Accept=application/json")
    public void actualizarGrupo(@RequestBody GrupoDto grupoRq) {
        grupoService.actualizarGrupo(grupoRq);
    }

    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public List<GrupoDto> listarGrupos() {
        return grupoService.findAll();
    }

    // Petición para eliminar un Grupo
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public void eliminarGrupo(@PathVariable Long id) {
        System.out.println("Grupo a eliminar el id: " + id);
        grupoService.eliminarGrupo(id);
    }
}
