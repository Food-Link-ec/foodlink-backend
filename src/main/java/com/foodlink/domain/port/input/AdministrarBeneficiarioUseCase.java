package com.foodlink.domain.port.input;

import com.foodlink.application.dto.response.BeneficiarioResponse;

import java.util.List;
import java.util.UUID;

public interface AdministrarBeneficiarioUseCase {

    BeneficiarioResponse verificar(UUID beneficiarioId);

    BeneficiarioResponse rechazar(UUID beneficiarioId);

    List<BeneficiarioResponse> listarPendientes();

    List<BeneficiarioResponse> listarTodos();
}