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
    public List<LoteResponse> obtenerMisLotes(UUID comercioId, String estado) {
        List<LoteExcedente> lotes;

        if (estado != null && !estado.isBlank()) {
            lotes = repositorioLote.buscarPorComercioYEstado(
                    comercioId, EstadoLote.valueOf(estado.toUpperCase()));
        } else {
            lotes = repositorioLote.buscarPorComercio(comercioId);
        }

        return lotes.stream()
                .map(this::toResponse)
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
