package com.foodlink.application.dto.response;

import java.util.UUID;

public record EstadisticasCompradorResponse(
        UUID compradorId,
        long totalLotesComprados,
        double totalPagado,
        double totalKgAdquiridos,
        double ahorroEstimado,
        String mensajeAhorro
) {
}
