package com.foodlink.domain.port.input;

import com.foodlink.application.dto.response.LoteResponse;

import java.util.List;
import java.util.UUID;

public interface MisLotesUseCase {

    List<LoteResponse> obtenerMisLotes(UUID comercioId);

    List<LoteResponse> obtenerMisReservas(UUID usuarioId);
}
