package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.domain.model.beneficiario.EstadoVerificacion;
import com.foodlink.infrastructure.adapter.output.persistence.entity.BeneficiarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeneficiarioJpaRepository extends JpaRepository<BeneficiarioJpaEntity, UUID> {

    Optional<BeneficiarioJpaEntity> findByRuc(String ruc);

    Optional<BeneficiarioJpaEntity> findByEmail(String email);

    boolean existsByRuc(String ruc);

    List<BeneficiarioJpaEntity> findByEstadoVerificacion(EstadoVerificacion estadoVerificacion);
}