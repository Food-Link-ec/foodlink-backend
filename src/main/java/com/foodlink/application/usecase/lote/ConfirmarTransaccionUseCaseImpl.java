package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.ConfirmarDonacionRequest;
import com.foodlink.application.dto.request.ConfirmarVentaRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.input.ConfirmarTransaccionUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ConfirmarTransaccionUseCaseImpl implements ConfirmarTransaccionUseCase {

    private final IRepositorioLote repositorioLote;
    private final ApplicationEventPublisher eventPublisher;

    public ConfirmarTransaccionUseCaseImpl(
            IRepositorioLote repositorioLote,
            ApplicationEventPublisher eventPublisher) {
        this.repositorioLote = repositorioLote;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public LoteResponse confirmarVenta(ConfirmarVentaRequest request) {
        LoteExcedente lote = repositorioLote.buscarPorId(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lote no encontrado: " + request.loteId()));
        lote.registrarVenta(request.compradorId());
        LoteExcedente guardado = repositorioLote.guardar(lote);
        lote.pullEventos().forEach(eventPublisher::publishEvent);
        return toResponse(guardado);
    }

    @Override
    public LoteResponse confirmarDonacion(ConfirmarDonacionRequest request) {
        LoteExcedente lote = repositorioLote.buscarPorId(request.loteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lote no encontrado: " + request.loteId()));
        lote.registrarDonacion(request.organizacionId());
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
