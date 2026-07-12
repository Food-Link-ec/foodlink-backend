package com.foodlink.application.dto.response;

import java.util.List;

public record AnalyticsDashboardResponse(
        long totalComerciosRegistrados,
        long totalComerciosVerificados,
        long totalBeneficiariosVerificados,
        long totalCompradoresActivos,
        long totalLotesPublicados,
        long totalLotesDisponibles,
        long totalLotesReservados,
        long totalLotesEntregados,
        long totalLotesExpirados,
        double totalKgRescatados,
        double totalCo2EvitadoKg,
        int totalPersonasBeneficiadas,
        long totalLotesEntregadosMesActual,
        double kgRescatadosMesActual,
        List<ComercioTopResponse> top5Comercios
) {
}
