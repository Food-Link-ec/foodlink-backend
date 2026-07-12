package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.response.LoteResponse;

import java.util.List;
import java.util.UUID;

public interface BuscarLotesUseCase {

    List<LoteResponse> buscar(BuscarLotesRequest request);

    LoteResponse buscarPorId(UUID id);
}