package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.CancelarReservaRequest;
import com.foodlink.application.dto.request.ReservarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;

import java.util.UUID;

public interface ReservarLoteUseCase {

    LoteResponse reservar(ReservarLoteRequest request, UUID usuarioId);

    LoteResponse cancelarReserva(CancelarReservaRequest request);
}