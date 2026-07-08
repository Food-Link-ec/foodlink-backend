package com.foodlink.domain.port.output;

import com.foodlink.domain.model.comprador.Comprador;

import java.util.Optional;
import java.util.UUID;

public interface IRepositorioComprador {

    Comprador guardar(Comprador comprador);

    Comprador guardar(Comprador comprador, String passwordHash);

    Optional<Comprador> buscarPorId(UUID id);

    Optional<Comprador> buscarPorCedula(String cedula);

    boolean existePorCedula(String cedula);
}