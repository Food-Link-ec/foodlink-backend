package com.foodlink.application.dto.response;

import java.util.List;
import java.util.UUID;

public record ResumenValoracionesResponse(
        UUID comercioId,
        String nombreComercio,
        double promedioEstrellas,
        long totalValoraciones,
        List<ValoracionResponse> valoraciones
) {
}
