package com.foodlink.domain.port.input;

import com.foodlink.application.dto.response.MiPerfilResponse;

import java.util.UUID;

public interface MiPerfilUseCase {

    MiPerfilResponse obtenerMiPerfil(UUID usuarioId, String tipoUsuario);
}
