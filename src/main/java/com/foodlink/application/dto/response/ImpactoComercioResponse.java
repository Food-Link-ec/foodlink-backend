package com.foodlink.application.dto.response;

import java.util.UUID;

public record ImpactoComercioResponse(
        UUID comercioId,
        String nombreComercio,
        double totalKgRescatados,
        double totalCo2EvitadoKg,
        int totalPersonasBeneficiadas,
        long totalLotesEntregados,
        double kgRescatadosMesActual,
        String tituloLogro,
        String descripcionLogro,
        String mensajeImpacto
) {
}
