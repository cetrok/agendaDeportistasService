package com.agendadeportistas.agendaservices.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.agendadeportistas.agendaservices.entities.DeportistaEntity;
import com.agendadeportistas.agendaservices.entities.PaqueteClasesEntity;
import com.agendadeportistas.agendaservices.repositories.DeportistaRepository;
import com.agendadeportistas.agendaservices.repositories.PaqueteClasesRepository;
import com.agendadeportistas.agendaservices.shared.dto.DeportistaLightDto;
import com.agendadeportistas.agendaservices.shared.dto.PaqueteClasesDto;

@ExtendWith(MockitoExtension.class)
class PaqueteClasesServiceTest {

    @Mock
    private PaqueteClasesRepository paqueteClasesRepository;

    @Mock
    private DeportistaRepository deportistaRepository;

    @InjectMocks
    private PaqueteClasesService paqueteClasesService;

    private DeportistaEntity dep1;
    private DeportistaEntity dep2;

    @BeforeEach
    void setUp() {
        dep1 = new DeportistaEntity();
        dep1.setId("D001");
        dep1.setNombre("Ana Martínez");

        dep2 = new DeportistaEntity();
        dep2.setId("D002");
        dep2.setNombre("Carlos López");
    }

    private PaqueteClasesEntity paquete(Long id, DeportistaEntity dep, int total, int restantes, boolean activo) {
        PaqueteClasesEntity p = new PaqueteClasesEntity();
        p.setIdPaquete(id);
        p.setDeportista(dep);
        p.setTotalClases(total);
        p.setClasesRestantes(restantes);
        p.setFechaPago("2024-01-01");
        p.setActivo(activo);
        return p;
    }

    private PaqueteClasesDto dtoConDeportista(String deportistaId, int totalClases) {
        DeportistaLightDto light = new DeportistaLightDto();
        light.setId(deportistaId);
        PaqueteClasesDto dto = new PaqueteClasesDto();
        dto.setDeportista(light);
        dto.setTotalClases(totalClases);
        dto.setFechaPago("2024-06-01");
        return dto;
    }

    // ─── crearPaquete ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("crearPaquete")
    class CrearPaquete {

        @Test
        @DisplayName("crea el paquete con clasesRestantes = totalClases y activo = true")
        void deportistaExiste_creaCorrectamente() {
            PaqueteClasesDto dto = dtoConDeportista("D001", 10);
            when(deportistaRepository.findById("D001")).thenReturn(Optional.of(dep1));

            PaqueteClasesEntity saved = paquete(1L, dep1, 10, 10, true);
            when(paqueteClasesRepository.save(any())).thenReturn(saved);

            PaqueteClasesDto resultado = paqueteClasesService.crearPaquete(dto);

            ArgumentCaptor<PaqueteClasesEntity> captor = ArgumentCaptor.forClass(PaqueteClasesEntity.class);
            verify(paqueteClasesRepository).save(captor.capture());
            PaqueteClasesEntity guardado = captor.getValue();

            assertThat(guardado.getClasesRestantes()).isEqualTo(10);
            assertThat(guardado.getTotalClases()).isEqualTo(10);
            assertThat(guardado.isActivo()).isTrue();
            assertThat(guardado.getDeportista()).isSameAs(dep1);
            assertThat(resultado.getIdPaquete()).isEqualTo(1L);
            assertThat(resultado.getDeportista().getId()).isEqualTo("D001");
        }

        @Test
        @DisplayName("lanza RuntimeException cuando el deportista no existe")
        void deportistaNoExiste_lanzaExcepcion() {
            PaqueteClasesDto dto = dtoConDeportista("INEXISTENTE", 5);
            when(deportistaRepository.findById("INEXISTENTE")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paqueteClasesService.crearPaquete(dto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Deportista no encontrado");

            verify(paqueteClasesRepository, never()).save(any());
        }
    }

    // ─── obtenerPaquetesPorDeportista ────────────────────────────────────────

    @Nested
    @DisplayName("obtenerPaquetesPorDeportista")
    class ObtenerPaquetesPorDeportista {

        @Test
        @DisplayName("retorna la lista de paquetes mapeados a DTO con datos correctos")
        void retornaListaMapeada() {
            List<PaqueteClasesEntity> lista = List.of(
                    paquete(2L, dep1, 10, 10, true),
                    paquete(1L, dep1, 5, 0, false)
            );
            when(paqueteClasesRepository.findByDeportista_IdOrderByIdPaqueteDesc("D001")).thenReturn(lista);

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerPaquetesPorDeportista("D001");

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getIdPaquete()).isEqualTo(2L);
            assertThat(resultado.get(0).isActivo()).isTrue();
            assertThat(resultado.get(1).getIdPaquete()).isEqualTo(1L);
            assertThat(resultado.get(1).isActivo()).isFalse();
            assertThat(resultado.get(0).getDeportista().getId()).isEqualTo("D001");
        }

        @Test
        @DisplayName("retorna lista vacía cuando el deportista no tiene paquetes")
        void sinPaquetes_retornaVacio() {
            when(paqueteClasesRepository.findByDeportista_IdOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Collections.emptyList());

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerPaquetesPorDeportista("D001");

            assertThat(resultado).isEmpty();
        }
    }

    // ─── descontarClase ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("descontarClase")
    class DescontarClase {

        @Test
        @DisplayName("decrementa clasesRestantes en 1 y guarda el paquete")
        void conClasesRestantes_decrementa() {
            PaqueteClasesEntity p = paquete(1L, dep1, 10, 5, true);
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.of(p));

            paqueteClasesService.descontarClase("D001");

            assertThat(p.getClasesRestantes()).isEqualTo(4);
            assertThat(p.isActivo()).isTrue();
            verify(paqueteClasesRepository).save(p);
        }

        @Test
        @DisplayName("cuando era la última clase, marca el paquete como inactivo")
        void ultimaClase_desactivaPaquete() {
            PaqueteClasesEntity p = paquete(1L, dep1, 10, 1, true);
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.of(p));

            paqueteClasesService.descontarClase("D001");

            assertThat(p.getClasesRestantes()).isEqualTo(0);
            assertThat(p.isActivo()).isFalse();
            verify(paqueteClasesRepository).save(p);
        }

        @Test
        @DisplayName("cuando no hay paquete activo, no hace nada ni guarda nada")
        void sinPaqueteActivo_noHaceNada() {
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.empty());

            paqueteClasesService.descontarClase("D001");

            verify(paqueteClasesRepository, never()).save(any());
        }

        @Test
        @DisplayName("clasesRestantes no baja de 0 aunque ya esté en cero")
        void clasesEnCero_noDecrementaANegativo() {
            PaqueteClasesEntity p = paquete(1L, dep1, 10, 0, true);
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.of(p));

            paqueteClasesService.descontarClase("D001");

            assertThat(p.getClasesRestantes()).isZero();
        }
    }

    // ─── tieneActivoPaquete ──────────────────────────────────────────────────

    @Nested
    @DisplayName("tieneActivoPaquete")
    class TieneActivoPaquete {

        @Test
        @DisplayName("retorna true cuando existe un paquete activo para el deportista")
        void conPaqueteActivo_retornaTrue() {
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.of(paquete(1L, dep1, 10, 5, true)));

            assertThat(paqueteClasesService.tieneActivoPaquete("D001")).isTrue();
        }

        @Test
        @DisplayName("retorna false cuando no existe paquete activo para el deportista")
        void sinPaqueteActivo_retornaFalse() {
            when(paqueteClasesRepository.findTopByDeportista_IdAndActivoTrueOrderByIdPaqueteDesc("D001"))
                    .thenReturn(Optional.empty());

            assertThat(paqueteClasesService.tieneActivoPaquete("D001")).isFalse();
        }
    }

    // ─── obtenerIdsSinPaqueteActivo ──────────────────────────────────────────

    @Nested
    @DisplayName("obtenerIdsSinPaqueteActivo")
    class ObtenerIdsSinPaqueteActivo {

        @Test
        @DisplayName("excluye deportistas con paquete activo y retorna solo los que no tienen")
        void retornaSoloLosSinPaqueteActivo() {
            when(paqueteClasesRepository.findByActivo(true))
                    .thenReturn(List.of(paquete(1L, dep1, 10, 5, true)));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1, dep2));

            List<String> resultado = paqueteClasesService.obtenerIdsSinPaqueteActivo();

            assertThat(resultado).containsExactly("D002");
        }

        @Test
        @DisplayName("cuando todos tienen paquete activo, retorna lista vacía")
        void todosConPaqueteActivo_retornaVacio() {
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(List.of(
                    paquete(1L, dep1, 10, 5, true),
                    paquete(2L, dep2, 8, 3, true)
            ));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1, dep2));

            List<String> resultado = paqueteClasesService.obtenerIdsSinPaqueteActivo();

            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("cuando ninguno tiene paquete activo, retorna todos los IDs")
        void ningúnoConPaquete_retornaTodos() {
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(Collections.emptyList());
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1, dep2));

            List<String> resultado = paqueteClasesService.obtenerIdsSinPaqueteActivo();

            assertThat(resultado).containsExactlyInAnyOrder("D001", "D002");
        }
    }

    // ─── obtenerDeportistasConPaqueteAgotado ─────────────────────────────────

    @Nested
    @DisplayName("obtenerDeportistasConPaqueteAgotado")
    class ObtenerDeportistasConPaqueteAgotado {

        @Test
        @DisplayName("deportista con paquete agotado y sin paquete activo aparece en la lista")
        void paqueteAgotado_aparece() {
            PaqueteClasesEntity agotado = paquete(1L, dep1, 10, 0, false);
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(Collections.emptyList());
            when(paqueteClasesRepository.findAll()).thenReturn(List.of(agotado));
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(List.of(agotado));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getDeportista().getId()).isEqualTo("D001");
            assertThat(resultado.get(0).getTotalClases()).isEqualTo(10);
            assertThat(resultado.get(0).isActivo()).isFalse();
        }

        @Test
        @DisplayName("deportista sin ningún paquete registrado aparece con totalClases = 0")
        void sinNingunPaquete_apareceConTotalCero() {
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(Collections.emptyList());
            when(paqueteClasesRepository.findAll()).thenReturn(Collections.emptyList());
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(Collections.emptyList());
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getDeportista().getId()).isEqualTo("D001");
            assertThat(resultado.get(0).getTotalClases()).isEqualTo(0);
            assertThat(resultado.get(0).getClasesRestantes()).isEqualTo(0);
            assertThat(resultado.get(0).isActivo()).isFalse();
        }

        @Test
        @DisplayName("deportista con paquete activo NO aparece en la lista")
        void conPaqueteActivo_noAparece() {
            PaqueteClasesEntity activo = paquete(1L, dep1, 10, 5, true);
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(List.of(activo));
            when(paqueteClasesRepository.findAll()).thenReturn(List.of(activo));
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(Collections.emptyList());
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("deportista con paquete agotado Y paquete activo simultáneos NO aparece")
        void conPaqueteAgotadoYActivo_noAparece() {
            PaqueteClasesEntity activo  = paquete(2L, dep1, 10, 3, true);
            PaqueteClasesEntity agotado = paquete(1L, dep1, 5, 0, false);
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(List.of(activo));
            when(paqueteClasesRepository.findAll()).thenReturn(List.of(activo, agotado));
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(List.of(agotado));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("deportista con múltiples paquetes agotados aparece solo una vez")
        void multiplesPaquetesAgotados_apareceUnaSolaVez() {
            PaqueteClasesEntity agotado1 = paquete(1L, dep1, 5, 0, false);
            PaqueteClasesEntity agotado2 = paquete(2L, dep1, 8, 0, false);
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(Collections.emptyList());
            when(paqueteClasesRepository.findAll()).thenReturn(List.of(agotado1, agotado2));
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(List.of(agotado1, agotado2));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getDeportista().getId()).isEqualTo("D001");
        }

        @Test
        @DisplayName("combina correctamente deportistas agotados y sin paquete en la misma lista")
        void combinaAgotadosYSinPaquete() {
            PaqueteClasesEntity agotado = paquete(1L, dep1, 10, 0, false);
            when(paqueteClasesRepository.findByActivo(true)).thenReturn(Collections.emptyList());
            when(paqueteClasesRepository.findAll()).thenReturn(List.of(agotado));
            when(paqueteClasesRepository.findByActivo(false)).thenReturn(List.of(agotado));
            when(deportistaRepository.findAll()).thenReturn(List.of(dep1, dep2));

            List<PaqueteClasesDto> resultado = paqueteClasesService.obtenerDeportistasConPaqueteAgotado();

            assertThat(resultado).hasSize(2);
            List<String> ids = resultado.stream()
                    .map(r -> r.getDeportista().getId())
                    .toList();
            assertThat(ids).containsExactlyInAnyOrder("D001", "D002");
        }
    }
}
