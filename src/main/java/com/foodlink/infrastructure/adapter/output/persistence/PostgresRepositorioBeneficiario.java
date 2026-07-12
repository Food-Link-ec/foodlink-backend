package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.beneficiario.EstadoVerificacion;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.BeneficiarioMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresRepositorioBeneficiario implements IRepositorioBeneficiario {

    private final BeneficiarioJpaRepository beneficiarioJpaRepository;
    private final BeneficiarioMapper beneficiarioMapper;

    public PostgresRepositorioBeneficiario(BeneficiarioJpaRepository beneficiarioJpaRepository, BeneficiarioMapper beneficiarioMapper) {
        this.beneficiarioJpaRepository = beneficiarioJpaRepository;
        this.beneficiarioMapper = beneficiarioMapper;
    }

    @Override
    public Beneficiario guardar(Beneficiario beneficiario) {
        var entityGuardada = beneficiarioJpaRepository.save(beneficiarioMapper.toEntity(beneficiario));
        return beneficiarioMapper.toDomain(entityGuardada);
    }

    @Override
    public Beneficiario guardar(Beneficiario beneficiario, String passwordHash) {
        var entity = beneficiarioMapper.toEntity(beneficiario);
        entity.setPasswordHash(passwordHash);
        entity.setActivo(true);
        var entityGuardada = beneficiarioJpaRepository.save(entity);
        return beneficiarioMapper.toDomain(entityGuardada);
    }

    @Override
    public Optional<Beneficiario> buscarPorId(UUID id) {
        return beneficiarioJpaRepository.findById(id).map(beneficiarioMapper::toDomain);
    }

    @Override
    public Optional<Beneficiario> buscarPorRuc(String ruc) {
        return beneficiarioJpaRepository.findByRuc(ruc).map(beneficiarioMapper::toDomain);
    }

    @Override
    public boolean existePorRuc(String ruc) {
        return beneficiarioJpaRepository.existsByRuc(ruc);
    }

    @Override
    public List<Beneficiario> buscarPorEstado(EstadoVerificacion estado) {
        return beneficiarioJpaRepository.findByEstadoVerificacion(estado).stream()
                .map(beneficiarioMapper::toDomain)
                .toList();
    }

    @Override
    public List<Beneficiario> buscarTodos() {
        return beneficiarioJpaRepository.findAll().stream()
                .map(beneficiarioMapper::toDomain)
                .toList();
    }
}