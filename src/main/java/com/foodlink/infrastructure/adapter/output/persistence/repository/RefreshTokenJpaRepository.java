package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, UUID> {

    Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);

    void deleteByUsuarioId(UUID usuarioId);

    void deleteByExpiraEnBefore(LocalDateTime fecha);
}
