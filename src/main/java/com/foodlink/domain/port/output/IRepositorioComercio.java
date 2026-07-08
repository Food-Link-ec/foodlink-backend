package com.foodlink.domain.port.output;

import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comercio.EstadoComercio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioComercio {

    Comercio guardar(Comercio comercio);

    Comercio guardar(Comercio comercio, String passwordHash);

    Optional<Comercio> buscarPorId(UUID id);

    Optional<Comercio> buscarPorRuc(String ruc);

    boolean existePorRuc(String ruc);

    List<Comercio> buscarPorEstado(EstadoComercio estado);
}