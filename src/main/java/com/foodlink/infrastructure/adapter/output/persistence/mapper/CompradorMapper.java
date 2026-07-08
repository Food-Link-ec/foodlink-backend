package com.foodlink.infrastructure.adapter.output.persistence.mapper;

import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.infrastructure.adapter.output.persistence.entity.CompradorJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CompradorMapper {

    public CompradorJpaEntity toEntity(Comprador comprador) {
        CompradorJpaEntity entity = new CompradorJpaEntity();
        entity.setId(comprador.getId());
        entity.setCedula(comprador.getCedula());
        entity.setNombre(comprador.getNombre());
        entity.setApellido(comprador.getApellido());
        entity.setEmail(comprador.getEmail());
        entity.setTelefono(comprador.getTelefono());
        entity.setActivo(comprador.estaActivo());
        entity.setFechaRegistro(comprador.getFechaRegistro());
        return entity;
    }

    public Comprador toDomain(CompradorJpaEntity entity) {
        return Comprador.reconstituir(
                entity.getId(),
                entity.getCedula(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getFechaRegistro(),
                entity.isActivo()
        );
    }
}