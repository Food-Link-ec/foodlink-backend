package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.EstadisticasCompradorResponse;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CompradorStatsService {

    private final LoteJpaRepository loteJpaRepository;
    private final ImpactoMetricaJpaRepository impactoMetricaJpaRepository;

    public CompradorStatsService(LoteJpaRepository loteJpaRepository,
                                  ImpactoMetricaJpaRepository impactoMetricaJpaRepository) {
        this.loteJpaRepository = loteJpaRepository;
        this.impactoMetricaJpaRepository = impactoMetricaJpaRepository;
    }

    public EstadisticasCompradorResponse obtenerEstadisticas(UUID compradorId) {
        Object[] stats = loteJpaRepository.estadisticasCompras(compradorId);

        long lotes = 0L;
        double pagado = 0.0;
        double kg = 0.0;

        if (stats != null && stats.length > 0) {
            Object[] row = (stats.length == 1 && stats[0] instanceof Object[])
                    ? (Object[]) stats[0]
                    : stats;
            if (row.length >= 3) {
                lotes  = row[0] != null ? ((Number) row[0]).longValue()    : 0L;
                pagado = row[1] != null ? ((Number) row[1]).doubleValue()   : 0.0;
                kg     = row[2] != null ? ((Number) row[2]).doubleValue()   : 0.0;
            }
        }

        double ahorro = Math.round(pagado * 1.5 * 100.0) / 100.0;

        // CO2: 2.5 kg CO2 por kg de alimento rescatado (misma fórmula que ImpactoService)
        Double co2Raw = impactoMetricaJpaRepository.sumCo2ByComprador(compradorId);
        double co2 = co2Raw != null ? Math.round(co2Raw * 10.0) / 10.0 : Math.round(kg * 2.5 * 10.0) / 10.0;

        // Equivalencias ambientales
        double kmSinConducir = Math.round(co2 * 5.0 * 10.0) / 10.0; // 1 kg CO2 ≈ 5 km
        int arboles = (int) Math.ceil(co2 / 6.0); // 1 árbol absorbe ~6 kg CO2/año

        // Ahorro últimos 7 meses (mes 0 = hace 6 meses, mes 6 = mes actual)
        List<Double> ahorroMeses = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Double pagadoMes = loteJpaRepository.sumPagadoMes(compradorId, i);
            double ahorroMes = pagadoMes != null ? Math.round(pagadoMes * 1.5 * 100.0) / 100.0 : 0.0;
            ahorroMeses.add(ahorroMes);
        }

        String mensaje = lotes > 0
                ? "Has ahorrado aproximadamente $" + ahorro +
                  " comprando " + lotes + " lotes de calidad a precio reducido."
                : "Aún no has realizado compras. Explora los lotes disponibles.";

        return new EstadisticasCompradorResponse(
                compradorId,
                lotes,
                Math.round(pagado * 100.0) / 100.0,
                Math.round(kg * 100.0) / 100.0,
                ahorro,
                mensaje,
                co2,
                kmSinConducir,
                arboles,
                ahorroMeses
        );
    }
}
