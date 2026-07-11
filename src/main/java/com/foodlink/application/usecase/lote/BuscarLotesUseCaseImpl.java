package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.input.BuscarLotesUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BuscarLotesUseCaseImpl implements BuscarLotesUseCase {

    private final IRepositorioLote repositorioLote;

    public BuscarLotesUseCaseImpl(IRepositorioLote repositorioLote) {
        this.repositorioLote = repositorioLote;
    }

    @Override
    public List<LoteResponse> buscar(BuscarLotesRequest request) {
        List<LoteExcedente> lotes;

        if (request.comercioId() != null) {
            lotes = repositorioLote.buscarPorComercio(request.comercioId());
        } else if (request.modalidad() != null) {
            Modalidad modalidad = Modalidad.valueOf(request.modalidad().toUpperCase());
            lotes = repositorioLote.buscarDisponiblesPorModalidad(modalidad);
        } else if (request.estado() != null) {
            EstadoLote estado = EstadoLote.valueOf(request.estado().toUpperCase());
            lotes = repositorioLote.buscarPorEstado(estado);
        } else {
            lotes = repositorioLote.buscarDisponibles();
        }

        return lotes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoteResponse buscarPorId(UUID id) {
        LoteExcedente lote = repositorioLote.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado: " + id));
        return toResponse(lote);
    }

    private LoteResponse toResponse(LoteExcedente lote) {
        return new LoteResponse(
                lote.getId(),
                lote.getComercioId(),
                lote.getModalidad().name(),
                lote.getEstado().name(),
                lote.getCantidadKg(),
                lote.getModalidad() == Modalidad.VENTA && lote.getPrecio() != null
                        ? lote.getPrecio().getMonto() : null,
                null,
                lote.getFechaCaducidad().getValor(),
                lote.getFechaPublicacion(),
                lote.getDescripcion(),
                lote.getFotosUrl()
        );
    }
}