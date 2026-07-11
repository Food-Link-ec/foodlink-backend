package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.port.output.IRepositorioLote;
import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.LoteMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PostgresRepositorioLote implements IRepositorioLote {

    private final LoteJpaRepository jpaRepository;
    private final LoteMapper mapper;

    public PostgresRepositorioLote(LoteJpaRepository jpaRepository, LoteMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public LoteExcedente guardar(LoteExcedente lote) {
        LoteJpaEntity entity = mapper.toEntity(lote);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<LoteExcedente> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<LoteExcedente> buscarPorEstado(EstadoLote estado) {
        return jpaRepository.findByEstado(estado.name())
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<LoteExcedente> buscarPorComercio(UUID comercioId) {
        return jpaRepository.findByComercioId(comercioId)
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<LoteExcedente> buscarDisponibles() {
        return jpaRepository.findByEstado(EstadoLote.DISPONIBLE.name())
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<LoteExcedente> buscarExpirados() {
        return jpaRepository.findByEstado(EstadoLote.EXPIRADO.name())
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<LoteExcedente> buscarDisponiblesPorModalidad(Modalidad modalidad) {
        return jpaRepository
                .findByEstadoAndModalidad(EstadoLote.DISPONIBLE.name(), modalidad.name())
                .stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}