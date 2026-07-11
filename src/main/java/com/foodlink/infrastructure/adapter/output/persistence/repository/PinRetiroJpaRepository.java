package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.PinRetiroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PinRetiroJpaRepository extends JpaRepository<PinRetiroJpaEntity, UUID> {

    Optional<PinRetiroJpaEntity> findByLoteId(UUID loteId);

    Optional<PinRetiroJpaEntity> findByPin(String pin);

    void deleteByLoteId(UUID loteId);
}