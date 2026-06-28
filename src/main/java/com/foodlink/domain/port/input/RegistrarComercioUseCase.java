package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.RegistrarComercioRequest;
import com.foodlink.application.dto.response.ComercioResponse;

public interface RegistrarComercioUseCase {

    ComercioResponse registrar(RegistrarComercioRequest request);
}