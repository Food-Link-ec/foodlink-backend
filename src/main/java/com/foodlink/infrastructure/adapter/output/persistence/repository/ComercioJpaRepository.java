package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ComercioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComercioJpaRepository extends JpaRepository<ComercioJpaEntity, UUID> {

    Optional<ComercioJpaEntity> findByRuc(String ruc);

    Optional<ComercioJpaEntity> findByEmail(String email);

    boolean existsByRuc(String ruc);

    List<ComercioJpaEntity> findByEstado(EstadoComercio estado);

    Long countByEstado(EstadoComercio estado);
}