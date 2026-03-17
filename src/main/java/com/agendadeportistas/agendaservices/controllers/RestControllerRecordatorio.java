package com.agendadeportistas.agendaservices.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.agendadeportistas.agendaservices.entities.RecordatorioEntity;
import com.agendadeportistas.agendaservices.services.RecordatorioService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/recordatorios/")
public class RestControllerRecordatorio {
    @Autowired
    RecordatorioService recordatorioService;

    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<String> crearRecordatorio(@RequestBody RecordatorioEntity recordatorioRq) {
        // Crear el Recordatorio
        recordatorioService.crearRecordatorio(recordatorioRq);

        return new ResponseEntity<>("Recordatorio " + recordatorioRq.getTitulo() + " creado con exito",
                HttpStatus.CREATED);
    }

    // Petición para actualizar un Recordatorio
    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping(value = "actualizar", headers = "Accept=application/json")
    public void actualizarRecordatorio(@RequestBody RecordatorioEntity recordatorioRq) {
        recordatorioService.actualizarRecordatorio(recordatorioRq);
    }

    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public List<RecordatorioEntity> listarRecordatorios() {
        return recordatorioService.listarRecordatorios();
    }

    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping(value = "listarHoy", headers = "Accept=application/json")
    public List<RecordatorioEntity> listarRecordatoriosHoy() {
        return recordatorioService.listarRecordatoriosHoy();
    }

    // Petición para eliminar un Recordatorio
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public void eliminarRecordatorio(@PathVariable Long id) {
        recordatorioService.eliminarRecordatorio(id);
    }

    // Petición para eliminar recordatorios expirados
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping(value = "eliminarExpirados", headers = "Accept=application/json")
    public void eliminarRecordatorioExpirados() {
        recordatorioService.eliminarRecordatoriosExpirados();
    }
}
