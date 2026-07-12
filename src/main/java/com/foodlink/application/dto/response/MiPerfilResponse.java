package com.foodlink.application.dto.response;

import java.util.UUID;

public record MiPerfilResponse(
        UUID id,
        String email,
        String tipoUsuario,
        String rol,
        Object perfil
) {
}
