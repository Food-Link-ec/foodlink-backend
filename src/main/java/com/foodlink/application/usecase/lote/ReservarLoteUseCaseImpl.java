package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.CancelarReservaRequest;
import com.foodlink.application.dto.request.ReservarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.input.ReservarLoteUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservarLoteUseCaseImpl implements ReservarLoteUseCase {

    private final IRepositorioLote repositorioLote;
    private final ApplicationEventPublisher eventPublisher;

    public ReservarLoteUseCaseImpl(
            IRepositorioLote repositorioLote,
            ApplicationEventPublisher eventPublisher) {
        this.repositorioLote = repositorioLote;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public LoteResponse reservar(ReservarLoteRequest request, UUID usuarioId) {
        LoteExcedente lote = repositorioLote.buscarPorId(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lote no encontrado: " + request.loteId()));

        lote.reservar(usuarioId);
        LoteExcedente guardado = repositorioLote.guardar(lote);
        lote.pullEventos().forEach(eventPublisher::publishEvent);
        return toResponse(guardado);
    }

    @Override
    public LoteResponse cancelarReserva(CancelarReservaRequest request) {
        LoteExcedente lote = repositorioLote.buscarPorId(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lote no encontrado: " + request.loteId()));

        lote.cancelarReserva(request.motivo() != null
                ? request.motivo() : "Cancelado por el usuario");
        LoteExcedente guardado = repositorioLote.guardar(lote);
        lote.pullEventos().forEach(eventPublisher::publishEvent);
        return toResponse(guardado);
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
                lote.getPrecioOriginal(),
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