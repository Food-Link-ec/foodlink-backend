package com.foodlink.domain.port.output;

import com.foodlink.domain.model.shared.PinRetiro;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioPin {

    void guardar(UUID loteId, PinRetiro pin, LocalDateTime expiraEn);

    Optional<String> buscarPorLote(UUID loteId);

    boolean existePorLote(UUID loteId);

    void marcarUsado(UUID loteId);

    void eliminarPorLote(UUID loteId);
}