package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.ValidarPinRequest;

import java.util.UUID;

public interface ValidarPinUseCase {

    void validarPin(ValidarPinRequest request, UUID receptorId);
}