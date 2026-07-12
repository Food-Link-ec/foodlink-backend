package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.ComercioMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresRepositorioComercio implements IRepositorioComercio {

    private final ComercioJpaRepository comercioJpaRepository;
    private final ComercioMapper comercioMapper;

    public PostgresRepositorioComercio(ComercioJpaRepository comercioJpaRepository, ComercioMapper comercioMapper) {
        this.comercioJpaRepository = comercioJpaRepository;
        this.comercioMapper = comercioMapper;
    }

    @Override
    public Comercio guardar(Comercio comercio) {
        var entityGuardada = comercioJpaRepository.save(comercioMapper.toEntity(comercio));
        return comercioMapper.toDomain(entityGuardada);
    }

    @Override
    public Comercio guardar(Comercio comercio, String passwordHash) {
        var entity = comercioMapper.toEntity(comercio);
        entity.setPasswordHash(passwordHash);
        entity.setActivo(true);
        var entityGuardada = comercioJpaRepository.save(entity);
        return comercioMapper.toDomain(entityGuardada);
    }

    @Override
    public Optional<Comercio> buscarPorId(UUID id) {
        return comercioJpaRepository.findById(id).map(comercioMapper::toDomain);
    }

    @Override
    public Optional<Comercio> buscarPorRuc(String ruc) {
        return comercioJpaRepository.findByRuc(ruc).map(comercioMapper::toDomain);
    }

    @Override
    public boolean existePorRuc(String ruc) {
        return comercioJpaRepository.existsByRuc(ruc);
    }

    @Override
    public List<Comercio> buscarPorEstado(EstadoComercio estado) {
        return comercioJpaRepository.findByEstado(estado).stream()
                .map(comercioMapper::toDomain)
                .toList();
    }

    @Override
    public List<Comercio> buscarTodos() {
        return comercioJpaRepository.findAll().stream()
                .map(comercioMapper::toDomain)
                .toList();
    }
}