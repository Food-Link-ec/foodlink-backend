package com.foodlink.application.dto.request;

import java.util.UUID;

public record BuscarLotesRequest(
        String modalidad,
        String estado,
        UUID comercioId,
        Double latitud,
        Double longitud,
        Double radioKm,
        String q,
        int page,
        int size
) {
    public BuscarLotesRequest {
        if (page < 0) page = 0;
        if (size <= 0 || size > 50) size = 10;
    }
}
