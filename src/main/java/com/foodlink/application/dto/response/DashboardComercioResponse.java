package com.foodlink.application.dto.response;

import java.util.List;
import java.util.UUID;

public record DashboardComercioResponse(
        UUID comercioId,
        String nombreComercio,
        // Hero
        double ingresosMesActual,
        double ingresosMesAnterior,
        double porcentajeCambioIngresos,
        // Stats principales
        double totalKgRescatados,
        long reservasPendientes,
        long lotesActivos,
        int personasBeneficiadasMes,
        // Lotes recientes (últimos 4)
        List<LoteResumenDto> lotesRecientes,
        // Reservas urgentes de donación (máx 3)
        List<ReservaUrgentDto> reservasUrgentes,
        // Impacto
        double co2EvitadoKg,
        long totalLotesEntregados,
        String tituloLogro,
        String descripcionLogro
) {
    public record LoteResumenDto(
            String id,
            String nombre,
            String cantidad,
            String hace,
            String estado,
            String fotoUrl
    ) {}

    public record ReservaUrgentDto(
            String loteId,
            String organizacion,
            String producto,
            String hora
    ) {}
}
