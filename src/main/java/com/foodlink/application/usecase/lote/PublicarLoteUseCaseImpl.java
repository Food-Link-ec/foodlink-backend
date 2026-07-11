package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.PublicarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.FabricaLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.domain.port.input.PublicarLoteUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PublicarLoteUseCaseImpl implements PublicarLoteUseCase {

    private final IRepositorioLote repositorioLote;
    private final ApplicationEventPublisher eventPublisher;

    public PublicarLoteUseCaseImpl(
            IRepositorioLote repositorioLote,
            ApplicationEventPublisher eventPublisher) {
        this.repositorioLote = repositorioLote;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public LoteResponse publicar(PublicarLoteRequest request, UUID comercioId) {
        Modalidad modalidad = Modalidad.valueOf(request.modalidad().toUpperCase());
        FechaCaducidad fechaCaducidad = FechaCaducidad.de(request.fechaCaducidad());

        LoteExcedente lote = switch (modalidad) {
            case VENTA -> FabricaLote.crearParaVenta(
                    comercioId,
                    request.cantidadKg(),
                    Dinero.de(request.precio(), "USD"),
                    Dinero.de(request.precioMercado(), "USD"),
                    fechaCaducidad,
                    request.descripcion(),
                    request.fotosUrl()
            );
            case DONACION -> FabricaLote.crearParaDonacion(
                    comercioId,
                    request.cantidadKg(),
                    fechaCaducidad,
                    request.descripcion(),
                    request.fotosUrl()
            );
            case RETIRO_DIRECTO -> FabricaLote.crearParaRetiroDirecto(
                    comercioId,
                    request.cantidadKg(),
                    fechaCaducidad,
                    request.descripcion(),
                    request.fotosUrl()
            );
        };

        lote.publicar();
        LoteExcedente guardado = repositorioLote.guardar(lote);
        lote.pullEventos().forEach(eventPublisher::publishEvent);
        return toResponse(guardado, request.precioMercado());
    }

    private LoteResponse toResponse(LoteExcedente lote, BigDecimal precioMercado) {
        return new LoteResponse(
                lote.getId(),
                lote.getComercioId(),
                lote.getModalidad().name(),
                lote.getEstado().name(),
                lote.getCantidadKg(),
                lote.getModalidad() == Modalidad.VENTA ? lote.getPrecio().getMonto() : null,
                precioMercado,
                lote.getFechaCaducidad().getValor(),
                lote.getFechaPublicacion(),
                lote.getDescripcion(),
                lote.getFotosUrl()
        );
    }
}