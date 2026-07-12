package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ResumenValoracionesResponse;
import com.foodlink.application.dto.response.ValoracionResponse;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ValoracionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ValoracionService {

    private final ValoracionJpaRepository jpaRepository;
    private final IRepositorioComercio repositorioComercio;

    public ValoracionService(
            ValoracionJpaRepository jpaRepository,
            IRepositorioComercio repositorioComercio) {
        this.jpaRepository = jpaRepository;
        this.repositorioComercio = repositorioComercio;
    }

    public ResumenValoracionesResponse obtenerValoraciones(UUID comercioId) {
        String nombre = repositorioComercio.buscarPorId(comercioId)
                .map(c -> c.getNombre().valor())
                .orElse("Comercio");

        double promedio = jpaRepository.promedioByComercio(comercioId) != null
                ? jpaRepository.promedioByComercio(comercioId) : 0.0;
        long total = jpaRepository.countByComercio(comercioId) != null
                ? jpaRepository.countByComercio(comercioId) : 0L;

        List<ValoracionResponse> lista = jpaRepository.findByComercioId(comercioId)
                .stream()
                .map(v -> new ValoracionResponse(
                        v.getId(), v.getLoteId(), v.getComercioId(),
                        v.getPuntuacion(), v.getComentario(), v.getCreadoEn()))
                .collect(Collectors.toList());

        return new ResumenValoracionesResponse(
                comercioId, nombre,
                Math.round(promedio * 10.0) / 10.0,
                total, lista);
    }

    public ResumenValoracionesResponse obtenerMisValoraciones(UUID comercioId) {
        return obtenerValoraciones(comercioId);
    }
}
