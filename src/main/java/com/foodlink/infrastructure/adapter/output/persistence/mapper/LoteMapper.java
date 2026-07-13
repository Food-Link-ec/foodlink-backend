package com.foodlink.infrastructure.adapter.output.persistence.mapper;

import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteFotoJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteJpaEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoteMapper {

    public LoteJpaEntity toEntity(LoteExcedente lote) {
        LoteJpaEntity entity = new LoteJpaEntity();
        entity.setId(lote.getId());
        entity.setComercioId(lote.getComercioId());
        entity.setModalidad(lote.getModalidad().name());
        entity.setEstado(lote.getEstado().name());
        entity.setCantidadKg(lote.getCantidadKg());
        if (lote.getPrecio() != null) {
            entity.setPrecioMonto(lote.getPrecio().getMonto());
            entity.setPrecioMoneda(lote.getPrecio().getMoneda());
        }
        entity.setFechaCaducidad(lote.getFechaCaducidad().getValor());
        entity.setFechaPublicacion(lote.getFechaPublicacion());
        entity.setDescripcion(lote.getDescripcion());
        entity.setBeneficiarioReservaId(lote.getBeneficiarioReservaId());
        entity.setInicioReserva(lote.getInicioReserva());
        entity.setLatitud(lote.getLatitud());
        entity.setLongitud(lote.getLongitud());
        entity.setCategoria(lote.getCategoria());
        entity.setCreadoEn(LocalDateTime.now());
        entity.setActualizadoEn(LocalDateTime.now());

        List<LoteFotoJpaEntity> fotos = lote.getFotosUrl().stream()
                .map(url -> {
                    LoteFotoJpaEntity foto = new LoteFotoJpaEntity();
                    foto.setLote(entity);
                    foto.setUrl(url);
                    return foto;
                }).collect(Collectors.toList());
        entity.setFotos(fotos);
        return entity;
    }

    public LoteExcedente toDomain(LoteJpaEntity entity) {
        List<String> fotosUrl = entity.getFotos().stream()
                .map(LoteFotoJpaEntity::getUrl)
                .collect(Collectors.toList());

        Dinero precio = null;
        if (entity.getPrecioMonto() != null) {
            precio = Dinero.de(entity.getPrecioMonto(), entity.getPrecioMoneda());
        }

        return LoteExcedente.reconstituir(
                entity.getId(),
                entity.getComercioId(),
                Modalidad.valueOf(entity.getModalidad()),
                EstadoLote.valueOf(entity.getEstado()),
                entity.getCantidadKg(),
                precio,
                FechaCaducidad.reconstituir(entity.getFechaCaducidad()),
                entity.getFechaPublicacion(),
                entity.getDescripcion(),
                fotosUrl,
                entity.getBeneficiarioReservaId(),
                entity.getInicioReserva(),
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getCategoria()
        );
    }
}