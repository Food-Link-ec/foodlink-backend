package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.shared.PinRetiro;
import com.foodlink.domain.port.output.IRepositorioPin;
import com.foodlink.infrastructure.adapter.output.persistence.entity.PinRetiroJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.PinRetiroJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresRepositorioPin implements IRepositorioPin {

    private final PinRetiroJpaRepository jpaRepository;

    public PostgresRepositorioPin(PinRetiroJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(UUID loteId, PinRetiro pin, LocalDateTime expiraEn) {
        jpaRepository.findByLoteId(loteId)
                .ifPresent(existing -> jpaRepository.deleteByLoteId(loteId));

        PinRetiroJpaEntity entity = new PinRetiroJpaEntity();
        entity.setLoteId(loteId);
        entity.setPin(pin.valor());
        entity.setQrData(pin.generarQrData(loteId));
        entity.setUsado(false);
        entity.setExpiraEn(expiraEn);
        entity.setCreadoEn(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<String> buscarPorLote(UUID loteId) {
        return jpaRepository.findByLoteId(loteId)
                .filter(e -> !e.isUsado())
                .filter(e -> e.getExpiraEn().isAfter(LocalDateTime.now()))
                .map(PinRetiroJpaEntity::getPin);
    }

    @Override
    public boolean existePorLote(UUID loteId) {
        return jpaRepository.findByLoteId(loteId).isPresent();
    }

    @Override
    public void marcarUsado(UUID loteId) {
        jpaRepository.findByLoteId(loteId).ifPresent(e -> {
            e.setUsado(true);
            jpaRepository.save(e);
        });
    }

    @Override
    public void eliminarPorLote(UUID loteId) {
        jpaRepository.deleteByLoteId(loteId);
    }
}