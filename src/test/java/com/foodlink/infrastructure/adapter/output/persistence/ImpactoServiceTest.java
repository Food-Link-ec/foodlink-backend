package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ImpactoDashboardResponse;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ImpactoMetricaJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImpactoServiceTest {

    @Mock
    private ImpactoMetricaJpaRepository jpaRepository;

    @InjectMocks
    private ImpactoService impactoService;

    @Test
    void registrarConCincoKgDeberiaGuardarEntidadConCo2DoceyMedioYDiezPersonas() {
        ArgumentCaptor<ImpactoMetricaJpaEntity> captor = ArgumentCaptor.forClass(ImpactoMetricaJpaEntity.class);

        impactoService.registrar(UUID.randomUUID(), 5.0);

        verify(jpaRepository, times(1)).save(captor.capture());
        assertEquals(12.5, captor.getValue().getCo2EvitadoKg());
        assertEquals(10, captor.getValue().getPersonasBeneficiadas());
    }

    @Test
    void registrarConUnPuntoTresKgDeberiaCalcularTresPersonas() {
        ArgumentCaptor<ImpactoMetricaJpaEntity> captor = ArgumentCaptor.forClass(ImpactoMetricaJpaEntity.class);

        impactoService.registrar(UUID.randomUUID(), 1.3);

        verify(jpaRepository).save(captor.capture());
        assertEquals(3, captor.getValue().getPersonasBeneficiadas());
    }

    @Test
    void registrarConCeroPuntoUnoKgDeberiaCalcularUnaPersona() {
        ArgumentCaptor<ImpactoMetricaJpaEntity> captor = ArgumentCaptor.forClass(ImpactoMetricaJpaEntity.class);

        impactoService.registrar(UUID.randomUUID(), 0.1);

        verify(jpaRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getPersonasBeneficiadas());
    }

    @Test
    void consultarDashboardSinDatosDeberiaRetornarCerosYMensaje() {
        when(jpaRepository.sumCantidadKg()).thenReturn(null);
        when(jpaRepository.sumCo2EvitadoKg()).thenReturn(null);
        when(jpaRepository.sumPersonasBeneficiadas()).thenReturn(null);
        when(jpaRepository.countLotesEntregados()).thenReturn(null);

        ImpactoDashboardResponse response = impactoService.consultarDashboard();

        assertEquals(0.0, response.totalKgRescatados());
        assertEquals(0.0, response.totalCo2EvitadoKg());
        assertEquals(0, response.totalPersonasBeneficiadas());
        assertEquals(0L, response.totalLotesEntregados());
        assertTrue(response.mensaje().contains("0"));
    }

    @Test
    void consultarDashboardConDatosDeberiaRetornarSumasCorrectas() {
        when(jpaRepository.sumCantidadKg()).thenReturn(50.0);
        when(jpaRepository.sumCo2EvitadoKg()).thenReturn(125.0);
        when(jpaRepository.sumPersonasBeneficiadas()).thenReturn(100);
        when(jpaRepository.countLotesEntregados()).thenReturn(20L);

        ImpactoDashboardResponse response = impactoService.consultarDashboard();

        assertEquals(50.0, response.totalKgRescatados());
        assertEquals(125.0, response.totalCo2EvitadoKg());
        assertEquals(100, response.totalPersonasBeneficiadas());
        assertEquals(20L, response.totalLotesEntregados());
    }

    @Test
    void consultarDashboardMensajeDeberiaIncluirKgYPersonas() {
        when(jpaRepository.sumCantidadKg()).thenReturn(50.0);
        when(jpaRepository.sumCo2EvitadoKg()).thenReturn(125.0);
        when(jpaRepository.sumPersonasBeneficiadas()).thenReturn(100);
        when(jpaRepository.countLotesEntregados()).thenReturn(20L);

        ImpactoDashboardResponse response = impactoService.consultarDashboard();

        assertTrue(response.mensaje().contains("50"));
        assertTrue(response.mensaje().contains("100"));
    }
}