package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.application.dto.response.PageResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.input.BuscarLotesUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

        if (request.latitud() != null && request.longitud() != null && request.radioKm() != null) {
            lotes = repositorioLote.buscarDisponiblesCercanos(
                    request.latitud(), request.longitud(), request.radioKm());
        } else if (request.comercioId() != null) {
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

        if (request.categoria() != null && !request.categoria().isBlank()) {
            lotes = lotes.stream()
                    .filter(lote -> request.categoria().equalsIgnoreCase(lote.getCategoria()))
                    .collect(Collectors.toList());
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

    @Override
    public PageResponse<LoteResponse> buscarPaginado(BuscarLotesRequest request) {
        if (request.latitud() != null && request.longitud() != null && request.radioKm() != null) {
            List<LoteExcedente> todos = repositorioLote.buscarDisponiblesCercanos(
                    request.latitud(), request.longitud(), request.radioKm());

            int inicio = request.page() * request.size();
            int fin = Math.min(inicio + request.size(), todos.size());
            List<LoteExcedente> subLista = inicio >= todos.size()
                    ? Collections.emptyList()
                    : todos.subList(inicio, fin);

            List<LoteResponse> responses = subLista.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            Page<LoteResponse> page = new PageImpl<>(
                    responses,
                    PageRequest.of(request.page(), request.size()),
                    todos.size());

            return PageResponse.de(page);
        }

        return PageResponse.de(
                repositorioLote.buscarPaginado(request).map(this::toResponse));
    }

    @Override
    public List<LoteResponse> buscarHistorialExpirados(UUID comercioId) {
        return repositorioLote.buscarPorComercioYEstado(comercioId, EstadoLote.EXPIRADO)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
                lote.getFotosUrl(),
                lote.getLatitud(),
                lote.getLongitud(),
                lote.getCategoria(),
                lote.getBeneficiarioReservaId()
        );
    }
}