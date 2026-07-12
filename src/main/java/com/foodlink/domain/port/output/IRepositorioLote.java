package com.foodlink.domain.port.output;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
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

    List<LoteExcedente> buscarDisponiblesCercanos(double latitud, double longitud, double radioKm);

    List<LoteExcedente> buscarReservasExpiradas(LocalDateTime limiteInicio);

    List<LoteExcedente> buscarLotesCaducados();

    Page<LoteExcedente> buscarPaginado(BuscarLotesRequest request);

    List<LoteExcedente> buscarPorComercioYEstado(UUID comercioId, EstadoLote estado);
}
