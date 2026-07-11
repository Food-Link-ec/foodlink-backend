package com.foodlink.domain.port.output;

import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioLote {

    LoteExcedente guardar(LoteExcedente lote);

    Optional<LoteExcedente> buscarPorId(UUID id);

    List<LoteExcedente> buscarPorEstado(EstadoLote estado);

    List<LoteExcedente> buscarPorComercio(UUID comercioId);

    List<LoteExcedente> buscarDisponibles();

    List<LoteExcedente> buscarExpirados();

    List<LoteExcedente> buscarDisponiblesPorModalidad(Modalidad modalidad);
}
