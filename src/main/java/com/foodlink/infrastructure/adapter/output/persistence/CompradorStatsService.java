package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.EstadisticasCompradorResponse;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompradorStatsService {

    private final LoteJpaRepository loteJpaRepository;

    public CompradorStatsService(LoteJpaRepository loteJpaRepository) {
        this.loteJpaRepository = loteJpaRepository;
    }

    public EstadisticasCompradorResponse obtenerEstadisticas(UUID compradorId) {
        Object[] stats = loteJpaRepository.estadisticasCompras(compradorId);

        long lotes = 0L;
        double pagado = 0.0;
        double kg = 0.0;

        if (stats != null && stats.length >= 3) {
            lotes = stats[0] != null ? ((Number) stats[0]).longValue() : 0L;
            pagado = stats[1] != null ? ((Number) stats[1]).doubleValue() : 0.0;
            kg = stats[2] != null ? ((Number) stats[2]).doubleValue() : 0.0;
        }

        double ahorro = Math.round(pagado * 1.5 * 100.0) / 100.0;

        return new EstadisticasCompradorResponse(
                compradorId,
                lotes,
                Math.round(pagado * 100.0) / 100.0,
                Math.round(kg * 100.0) / 100.0,
                ahorro,
                lotes > 0
                        ? "Has ahorrado aproximadamente $" + ahorro +
                        " comprando " + lotes + " lotes de calidad a precio reducido."
                        : "Aún no has realizado compras. Explora los lotes disponibles."
        );
    }
}
