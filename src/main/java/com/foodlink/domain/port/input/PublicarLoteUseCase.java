package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.PublicarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;

import java.util.UUID;

public interface PublicarLoteUseCase {

    LoteResponse publicar(PublicarLoteRequest request, UUID comercioId);
}