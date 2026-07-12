package com.foodlink.application.dto.request;

public record AnalizarImagenRequest(
        String imagenBase64,
        String mimeType
) {
}
