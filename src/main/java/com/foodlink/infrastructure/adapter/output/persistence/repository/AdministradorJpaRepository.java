package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.AdministradorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdministradorJpaRepository extends JpaRepository<AdministradorJpaEntity, UUID> {

    Optional<AdministradorJpaEntity> findByEmail(String email);
}
