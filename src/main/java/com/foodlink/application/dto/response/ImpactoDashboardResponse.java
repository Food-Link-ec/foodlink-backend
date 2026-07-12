package com.foodlink.application.dto.response;

public record ImpactoDashboardResponse(
        double totalKgRescatados,
        double totalCo2EvitadoKg,
        int totalPersonasBeneficiadas,
        long totalLotesEntregados,
        String mensaje
) {
}