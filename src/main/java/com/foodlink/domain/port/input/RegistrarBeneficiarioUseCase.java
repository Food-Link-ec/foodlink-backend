package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.RegistrarBeneficiarioRequest;
import com.foodlink.application.dto.response.BeneficiarioResponse;

public interface RegistrarBeneficiarioUseCase {

    BeneficiarioResponse registrar(RegistrarBeneficiarioRequest request);
}