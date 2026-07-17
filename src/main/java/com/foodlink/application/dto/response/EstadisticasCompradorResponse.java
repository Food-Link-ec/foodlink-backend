package com.foodlink.application.dto.response;

import java.util.List;
import java.util.UUID;

public record EstadisticasCompradorResponse(
        UUID compradorId,
        long totalLotesComprados,
        double totalPagado,
        double totalKgAdquiridos,
        double ahorroEstimado,
        String mensajeAhorro,
        double co2EvitadoKg,
        double kmSinConducir,
        int arbolesEquivalentes,
        List<Double> ahorroUltimos7Meses
) {}
