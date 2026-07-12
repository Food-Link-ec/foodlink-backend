package com.foodlink.application.dto.response;

public record AnalisisImagenResponse(
        String fechaDetectada,
        String categoriaProducto,
        String descripcionSugerida,
        String confianzaFecha,
        String fuenteOcr,
        String mensaje
) {
}
