package com.foodlink.application.usecase.perfil;

import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.input.MisLotesUseCase;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MisLotesUseCaseImpl implements MisLotesUseCase {

    private final IRepositorioLote repositorioLote;

    public MisLotesUseCaseImpl(IRepositorioLote repositorioLote) {
        this.repositorioLote = repositorioLote;
    }

    @Override
    public List<LoteResponse> obtenerMisLotes(UUID comercioId) {
        return repositorioLote.buscarPorComercio(comercioId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoteResponse> obtenerMisReservas(UUID usuarioId) {
        return repositorioLote.buscarPorEstado(EstadoLote.RESERVADO)
                .stream()
                .filter(lote -> usuarioId.equals(lote.getBeneficiarioReservaId()))
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
                lote.getCategoria()
        );
    }
}
