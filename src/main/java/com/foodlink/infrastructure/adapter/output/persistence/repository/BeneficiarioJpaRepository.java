package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.BeneficiarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BeneficiarioJpaRepository extends JpaRepository<BeneficiarioJpaEntity, UUID> {

    Optional<BeneficiarioJpaEntity> findByRuc(String ruc);

    Optional<BeneficiarioJpaEntity> findByEmail(String email);

    boolean existsByRuc(String ruc);
}