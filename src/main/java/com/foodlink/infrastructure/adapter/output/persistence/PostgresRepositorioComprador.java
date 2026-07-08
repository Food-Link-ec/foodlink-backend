package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.domain.port.output.IRepositorioComprador;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.CompradorMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresRepositorioComprador implements IRepositorioComprador {

    private final CompradorJpaRepository compradorJpaRepository;
    private final CompradorMapper compradorMapper;

    public PostgresRepositorioComprador(CompradorJpaRepository compradorJpaRepository, CompradorMapper compradorMapper) {
        this.compradorJpaRepository = compradorJpaRepository;
        this.compradorMapper = compradorMapper;
    }

    @Override
    public Comprador guardar(Comprador comprador) {
        var entityGuardada = compradorJpaRepository.save(compradorMapper.toEntity(comprador));
        return compradorMapper.toDomain(entityGuardada);
    }

    @Override
    public Comprador guardar(Comprador comprador, String passwordHash) {
        var entity = compradorMapper.toEntity(comprador);
        entity.setPasswordHash(passwordHash);
        var entityGuardada = compradorJpaRepository.save(entity);
        return compradorMapper.toDomain(entityGuardada);
    }

    @Override
    public Optional<Comprador> buscarPorId(UUID id) {
        return compradorJpaRepository.findById(id).map(compradorMapper::toDomain);
    }

    @Override
    public Optional<Comprador> buscarPorCedula(String cedula) {
        return compradorJpaRepository.findByCedula(cedula).map(compradorMapper::toDomain);
    }

    @Override
    public boolean existePorCedula(String cedula) {
        return compradorJpaRepository.existsByCedula(cedula);
    }
}