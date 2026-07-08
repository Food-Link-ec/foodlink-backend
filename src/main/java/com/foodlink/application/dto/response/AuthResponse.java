package com.foodlink.application.dto.response;

import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tipoUsuario,
        UUID usuarioId,
        String email,
        String rol,
        long expiresIn
) {
}
