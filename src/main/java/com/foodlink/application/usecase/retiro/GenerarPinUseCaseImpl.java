package com.foodlink.application.usecase.retiro;

import com.foodlink.application.dto.response.PinRetiroResponse;
import com.foodlink.domain.model.shared.PinRetiro;
import com.foodlink.domain.port.input.GenerarPinUseCase;
import com.foodlink.domain.port.output.IRepositorioPin;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GenerarPinUseCaseImpl implements GenerarPinUseCase {

    private final IRepositorioPin repositorioPin;

    public GenerarPinUseCaseImpl(IRepositorioPin repositorioPin) {
        this.repositorioPin = repositorioPin;
    }

    @Override
    public PinRetiroResponse generarPin(UUID loteId) {
        if (repositorioPin.existePorLote(loteId)) {
            repositorioPin.eliminarPorLote(loteId);
        }
        PinRetiro pin = PinRetiro.generar();
        LocalDateTime expiraEn = LocalDateTime.now().plusHours(24);
        repositorioPin.guardar(loteId, pin, expiraEn);
        return new PinRetiroResponse(
                loteId,
                pin.valor(),
                pin.generarQrData(loteId),
                expiraEn,
                "PIN generado. Válido por 24 horas."
        );
    }
}