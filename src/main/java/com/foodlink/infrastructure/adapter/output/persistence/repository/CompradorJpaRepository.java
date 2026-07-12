package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.CompradorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompradorJpaRepository extends JpaRepository<CompradorJpaEntity, UUID> {

    Optional<CompradorJpaEntity> findByCedula(String cedula);

    Optional<CompradorJpaEntity> findByEmail(String email);

    boolean existsByCedula(String cedula);

    Long countByActivo(boolean activo);
}