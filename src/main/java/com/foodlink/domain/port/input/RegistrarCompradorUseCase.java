package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.RegistrarCompradorRequest;
import com.foodlink.application.dto.response.CompradorResponse;

public interface RegistrarCompradorUseCase {

    CompradorResponse registrar(RegistrarCompradorRequest request);
}