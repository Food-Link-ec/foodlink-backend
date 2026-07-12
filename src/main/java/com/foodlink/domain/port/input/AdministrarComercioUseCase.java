package com.foodlink.domain.port.input;

import com.foodlink.application.dto.response.ComercioResponse;

import java.util.List;
import java.util.UUID;

public interface AdministrarComercioUseCase {

    ComercioResponse verificar(UUID comercioId);

    ComercioResponse rechazar(UUID comercioId);

    ComercioResponse suspender(UUID comercioId);

    List<ComercioResponse> listarPendientes();

    List<ComercioResponse> listarTodos();
}