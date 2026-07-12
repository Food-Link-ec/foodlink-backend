package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.EstadisticasCompradorResponse;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompradorStatsServiceTest {

    @Mock
    private LoteJpaRepository loteJpaRepository;

    private CompradorStatsService construirServicio() {
        return new CompradorStatsService(loteJpaRepository);
    }

    @Test
    void obtenerEstadisticasConCeroComprasDeberiaRetornarTotalesEnCero() {
        UUID compradorId = UUID.randomUUID();
        when(loteJpaRepository.estadisticasCompras(compradorId))
                .thenReturn(new Object[]{0L, 0.0, 0.0});

        EstadisticasCompradorResponse response = construirServicio().obtenerEstadisticas(compradorId);

        assertEquals(0L, response.totalLotesComprados());
        assertEquals(0.0, response.ahorroEstimado());
    }

    @Test
    void obtenerEstadisticasConComprasDeberiaCalcularAhorroCorrectamente() {
        UUID compradorId = UUID.randomUUID();
        when(loteJpaRepository.estadisticasCompras(compradorId))
                .thenReturn(new Object[]{5L, 100.0, 25.0});

        EstadisticasCompradorResponse response = construirServicio().obtenerEstadisticas(compradorId);

        assertEquals(150.0, response.ahorroEstimado());
    }

    @Test
    void obtenerEstadisticasConComprasMensajeDeberiaIncluirNumeroDeLotes() {
        UUID compradorId = UUID.randomUUID();
        when(loteJpaRepository.estadisticasCompras(compradorId))
                .thenReturn(new Object[]{3L, 60.0, 15.0});

        EstadisticasCompradorResponse response = construirServicio().obtenerEstadisticas(compradorId);

        assertTrue(response.mensajeAhorro().contains("3 lotes"));
    }

    @Test
    void obtenerEstadisticasSinComprasMensajeDeberiaSerDeBienvenida() {
        UUID compradorId = UUID.randomUUID();
        when(loteJpaRepository.estadisticasCompras(compradorId))
                .thenReturn(new Object[]{0L, 0.0, 0.0});

        EstadisticasCompradorResponse response = construirServicio().obtenerEstadisticas(compradorId);

        assertEquals("Aún no has realizado compras. Explora los lotes disponibles.", response.mensajeAhorro());
    }
}
