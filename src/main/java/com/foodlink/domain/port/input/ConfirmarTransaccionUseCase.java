package com.foodlink.domain.port.input;

import com.foodlink.application.dto.request.ConfirmarDonacionRequest;
import com.foodlink.application.dto.request.ConfirmarVentaRequest;
import com.foodlink.application.dto.response.LoteResponse;

public interface ConfirmarTransaccionUseCase {

    LoteResponse confirmarVenta(ConfirmarVentaRequest request);

    LoteResponse confirmarDonacion(ConfirmarDonacionRequest request);
}
