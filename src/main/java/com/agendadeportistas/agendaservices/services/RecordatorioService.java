package com.agendadeportistas.agendaservices.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agendadeportistas.agendaservices.entities.RecordatorioEntity;
import com.agendadeportistas.agendaservices.repositories.RecordatorioRepository;

import jakarta.transaction.Transactional;

@Service
public class RecordatorioService {
    @Autowired
    private RecordatorioRepository recordatorioRepository;

    // Implementación de recordatorioService
    @Transactional
    public void crearRecordatorio(RecordatorioEntity recordatorioReq) {
        // Implementación de la lógica para crear un recordatorio
        RecordatorioEntity recordatorio = new RecordatorioEntity();
        recordatorio.setTitulo(recordatorioReq.getTitulo());
        recordatorio.setContenido(recordatorioReq.getContenido());
        recordatorio.setFechaRecordatorio(recordatorioReq.getFechaRecordatorio());
        recordatorio.setDiasRecordatorio(recordatorioReq.getDiasRecordatorio());
        recordatorio.setActivo(recordatorioReq.getFechaRecordatorio().after(new Date()));
        recordatorio.setCreado(new Date());

        recordatorioRepository.save(recordatorio);
    }

    @Transactional
    public void eliminarRecordatorio(Long idRecordatorio) {
        // Implementación de la lógica para eliminar un recordatorio
        recordatorioRepository.deleteById(idRecordatorio);
    }

    // Implementación de otros métodos para actualizar
    public void actualizarRecordatorio(RecordatorioEntity recordatorioReq) {
        RecordatorioEntity recordatorioEntity = recordatorioRepository
                .findById(recordatorioReq.getIdRecordatorio())
                .orElseThrow(() -> new RuntimeException("Recordatorio not found"));

        // Implementación de la lógica para crear un recordatorio

        recordatorioEntity.setTitulo(recordatorioReq.getTitulo());
        recordatorioEntity.setContenido(recordatorioReq.getContenido());
        recordatorioEntity.setFechaRecordatorio(recordatorioReq.getFechaRecordatorio());
        recordatorioEntity.setDiasRecordatorio(recordatorioReq.getDiasRecordatorio());
        recordatorioEntity.setActivo(recordatorioReq.getFechaRecordatorio().after(new Date()));
        recordatorioRepository.save(recordatorioEntity);
    }

    public List<RecordatorioEntity> listarRecordatorios() {
        // Implementación de la lógica para listar todos los recordatorios
        return recordatorioRepository.findAll();
    }

    public List<RecordatorioEntity> listarRecordatoriosHoy() {
        // Implementación de la lógica para listar los recordatorios que cumplen con la
        // regla
        Date today = new Date();
        return recordatorioRepository.findAll().stream()
                .filter(recordatorioEntity -> {
                    // Calculate the target date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(recordatorioEntity.getFechaRecordatorio());
                    Integer diasRecordatorio = Math.negateExact(recordatorioEntity.getDiasRecordatorio());

                    calendar.add(Calendar.DAY_OF_MONTH, diasRecordatorio);

                    // retornar si el recordatorio si la fecha calculada es menor a la fecha
                    // de hoy y la fecha recordatorio es mayor o igual a la fecha de hoy
                    return calendar.getTime().before(today) && recordatorioEntity.getFechaRecordatorio().after(today);
                })
                .toList();
    }

    public void eliminarRecordatoriosExpirados() {
        // Implementación de la lógica para marcar como inactivos los recordatorios con
        // fecha visible anterior a hoy
        List<RecordatorioEntity> recordatoriosExpirados = recordatorioRepository.findAll().stream()
                .filter(recordatorio -> recordatorio.getFechaRecordatorio().before(new Date()))
                .toList();
        for (RecordatorioEntity recordatorio : recordatoriosExpirados) {
            recordatorio.setActivo(false); // Asumiendo que tienes un campo 'activo' en tu entidad
            recordatorioRepository.save(recordatorio);
        }
    }

}
