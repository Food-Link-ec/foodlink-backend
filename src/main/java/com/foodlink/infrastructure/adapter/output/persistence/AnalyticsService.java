package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.AnalyticsDashboardResponse;
import com.foodlink.application.dto.response.ComercioTopResponse;
import com.foodlink.domain.model.beneficiario.EstadoVerificacion;
import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AnalyticsService {

    private final ComercioJpaRepository comercioRepo;
    private final BeneficiarioJpaRepository beneficiarioRepo;
    private final CompradorJpaRepository compradorRepo;
    private final LoteJpaRepository loteRepo;
    private final ImpactoMetricaJpaRepository impactoRepo;
    private final IRepositorioComercio repositorioComercio;

    public AnalyticsService(
            ComercioJpaRepository comercioRepo,
            BeneficiarioJpaRepository beneficiarioRepo,
            CompradorJpaRepository compradorRepo,
            LoteJpaRepository loteRepo,
            ImpactoMetricaJpaRepository impactoRepo,
            IRepositorioComercio repositorioComercio) {
        this.comercioRepo = comercioRepo;
        this.beneficiarioRepo = beneficiarioRepo;
        this.compradorRepo = compradorRepo;
        this.loteRepo = loteRepo;
        this.impactoRepo = impactoRepo;
        this.repositorioComercio = repositorioComercio;
    }

    public AnalyticsDashboardResponse obtenerAnalytics() {
        long comerciosTotal = comercioRepo.count();
        long comerciosVerificados = comercioRepo.countByEstado(EstadoComercio.VERIFICADO) != null
                ? comercioRepo.countByEstado(EstadoComercio.VERIFICADO) : 0L;
        long beneficiariosVerificados = beneficiarioRepo.countByEstadoVerificacion(EstadoVerificacion.VERIFICADO) != null
                ? beneficiarioRepo.countByEstadoVerificacion(EstadoVerificacion.VERIFICADO) : 0L;
        long compradoresActivos = compradorRepo.countByActivo(true) != null
                ? compradorRepo.countByActivo(true) : 0L;

        long lotesDisponibles = loteRepo.countByEstado("DISPONIBLE") != null
                ? loteRepo.countByEstado("DISPONIBLE") : 0L;
        long lotesReservados = loteRepo.countByEstado("RESERVADO") != null
                ? loteRepo.countByEstado("RESERVADO") : 0L;
        long lotesEntregados = loteRepo.countByEstado("ENTREGADO") != null
                ? loteRepo.countByEstado("ENTREGADO") : 0L;
        long lotesExpirados = loteRepo.countByEstado("EXPIRADO") != null
                ? loteRepo.countByEstado("EXPIRADO") : 0L;
        long lotesTotal = comercioRepo.count() > 0 ? loteRepo.count() : 0L;

        double kgTotal = impactoRepo.sumCantidadKg() != null
                ? impactoRepo.sumCantidadKg() : 0.0;
        double co2Total = impactoRepo.sumCo2EvitadoKg() != null
                ? impactoRepo.sumCo2EvitadoKg() : 0.0;
        int personasTotal = impactoRepo.sumPersonasBeneficiadas() != null
                ? impactoRepo.sumPersonasBeneficiadas() : 0;
        long entregadosMes = impactoRepo.countLotesEntregados() != null
                ? impactoRepo.countLotesEntregados() : 0L;

        double kgMes = 0.0;
        try {
            Double kgMesResult = impactoRepo.sumCantidadKgMesActual();
            kgMes = kgMesResult != null ? kgMesResult : 0.0;
        } catch (Exception ignored) {
        }

        List<ComercioTopResponse> top5 = new ArrayList<>();
        try {
            List<Object[]> rawTop = loteRepo.findTop5ComerciosPorImpacto();
            for (Object[] row : rawTop) {
                UUID cId = UUID.fromString(row[0].toString());
                long lotes = ((Number) row[1]).longValue();
                double kg = ((Number) row[2]).doubleValue();
                String nombre = repositorioComercio.buscarPorId(cId)
                        .map(c -> c.getNombre().valor())
                        .orElse("Comercio");
                top5.add(new ComercioTopResponse(cId, nombre, lotes, kg));
            }
        } catch (Exception ignored) {
        }

        return new AnalyticsDashboardResponse(
                comerciosTotal, comerciosVerificados,
                beneficiariosVerificados, compradoresActivos,
                lotesTotal, lotesDisponibles, lotesReservados,
                lotesEntregados, lotesExpirados,
                kgTotal, co2Total, personasTotal,
                entregadosMes, kgMes, top5);
    }
}
