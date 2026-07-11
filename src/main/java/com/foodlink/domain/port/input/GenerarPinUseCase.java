package com.foodlink.domain.port.input;

import com.foodlink.application.dto.response.PinRetiroResponse;

import java.util.UUID;

public interface GenerarPinUseCase {

    PinRetiroResponse generarPin(UUID loteId);
}