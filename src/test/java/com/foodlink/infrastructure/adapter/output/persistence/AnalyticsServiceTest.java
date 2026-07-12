package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.AnalyticsDashboardResponse;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private ComercioJpaRepository comercioRepo;

    @Mock
    private BeneficiarioJpaRepository beneficiarioRepo;

    @Mock
    private CompradorJpaRepository compradorRepo;

    @Mock
    private LoteJpaRepository loteRepo;

    @Mock
    private ImpactoMetricaJpaRepository impactoRepo;

    @Mock
    private IRepositorioComercio repositorioComercio;

    private AnalyticsService construirServicio() {
        return new AnalyticsService(comercioRepo, beneficiarioRepo, compradorRepo, loteRepo, impactoRepo, repositorioComercio);
    }

    @Test
    void obtenerAnalyticsDeberiaRetornarRespuestaNoNula() {
        AnalyticsDashboardResponse response = construirServicio().obtenerAnalytics();

        assertNotNull(response);
    }

    @Test
    void obtenerAnalyticsConRepositoriosVaciosDeberiaRetornarTodosLosConteosEnCero() {
        AnalyticsDashboardResponse response = construirServicio().obtenerAnalytics();

        assertEquals(0L, response.totalComerciosRegistrados());
        assertEquals(0L, response.totalComerciosVerificados());
        assertEquals(0L, response.totalBeneficiariosVerificados());
        assertEquals(0L, response.totalCompradoresActivos());
        assertEquals(0L, response.totalLotesDisponibles());
        assertEquals(0.0, response.totalKgRescatados());
    }

    @Test
    void obtenerAnalyticsDeberiaSumarCorrectamenteTotalKgRescatados() {
        when(impactoRepo.sumCantidadKg()).thenReturn(150.5);

        AnalyticsDashboardResponse response = construirServicio().obtenerAnalytics();

        assertEquals(150.5, response.totalKgRescatados());
    }

    @Test
    void top5ComerciosDeberiaSerListaVaciaSinLanzarExcepcion() {
        AnalyticsDashboardResponse response = construirServicio().obtenerAnalytics();

        assertNotNull(response.top5Comercios());
        assertTrue(response.top5Comercios().isEmpty());
    }
}
