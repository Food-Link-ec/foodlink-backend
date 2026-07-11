package com.foodlink.application.usecase.retiro;

import com.foodlink.application.dto.request.ValidarPinRequest;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.port.input.ValidarPinUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import com.foodlink.domain.port.output.IRepositorioPin;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidarPinUseCaseImpl implements ValidarPinUseCase {

    private final IRepositorioPin repositorioPin;
    private final IRepositorioLote repositorioLote;

    public ValidarPinUseCaseImpl(IRepositorioPin repositorioPin, IRepositorioLote repositorioLote) {
        this.repositorioPin = repositorioPin;
        this.repositorioLote = repositorioLote;
    }

    @Override
    public void validarPin(ValidarPinRequest request, UUID receptorId) {
        String pinGuardado = repositorioPin.buscarPorLote(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "PIN no encontrado o expirado para este lote"));

        if (!pinGuardado.equalsIgnoreCase(request.pin())) {
            throw new IllegalArgumentException("PIN inválido");
        }

        repositorioPin.marcarUsado(request.loteId());

        LoteExcedente lote = repositorioLote.buscarPorId(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado"));

        lote.confirmarEntrega(receptorId);
        repositorioLote.guardar(lote);
    }
}