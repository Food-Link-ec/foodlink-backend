package com.foodlink.infrastructure.adapter.output.persistence.mapper;

import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ComercioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ComercioMapper {

    public ComercioJpaEntity toEntity(Comercio comercio) {
        ComercioJpaEntity entity = new ComercioJpaEntity();
        entity.setId(comercio.getId());
        entity.setRuc(comercio.getRuc().valor());
        entity.setNombre(comercio.getNombre().valor());
        entity.setTelefono(comercio.getTelefono().valor());
        entity.setEmail(comercio.getEmail().valor());
        entity.setEstado(comercio.getEstado());
        entity.setFechaRegistro(comercio.getFechaRegistro());

        Direccion direccion = comercio.getDireccion();
        if (direccion != null) {
            entity.setProvincia(direccion.provincia());
            entity.setCiudad(direccion.ciudad());
            entity.setCallePrincipal(direccion.calle());
            entity.setReferencia(direccion.referencia());
        }

        return entity;
    }

    public Comercio toDomain(ComercioJpaEntity entity) {
        Direccion direccion = new Direccion(
                entity.getCallePrincipal(),
                entity.getCiudad(),
                entity.getProvincia(),
                entity.getReferencia(),
                null,
                null
        );

        return Comercio.reconstituir(
                entity.getId(),
                entity.getRuc(),
                entity.getNombre(),
                entity.getTelefono(),
                entity.getEmail(),
                direccion,
                entity.getEstado(),
                entity.getFechaRegistro()
        );
    }
}