package com.foodlink.domain.port.output;

import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.beneficiario.EstadoVerificacion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioBeneficiario {

    Beneficiario guardar(Beneficiario beneficiario);

    Beneficiario guardar(Beneficiario beneficiario, String passwordHash);

    Optional<Beneficiario> buscarPorId(UUID id);

    Optional<Beneficiario> buscarPorRuc(String ruc);

    boolean existePorRuc(String ruc);

    List<Beneficiario> buscarPorEstado(EstadoVerificacion estado);

    List<Beneficiario> buscarTodos();
}