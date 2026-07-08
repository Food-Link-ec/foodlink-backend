package com.foodlink.infrastructure.adapter.output.persistence.mapper;

import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.infrastructure.adapter.output.persistence.entity.BeneficiarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BeneficiarioMapper {

    public BeneficiarioJpaEntity toEntity(Beneficiario beneficiario) {
        BeneficiarioJpaEntity entity = new BeneficiarioJpaEntity();
        entity.setId(beneficiario.getId());
        entity.setNombre(beneficiario.getNombre());
        entity.setRuc(beneficiario.getRuc());
        entity.setEmail(beneficiario.getEmail());
        entity.setTelefono(beneficiario.getTelefono());
        entity.setEstadoVerificacion(beneficiario.getEstadoVerificacion());
        entity.setFechaRegistro(beneficiario.getFechaRegistro());

        Direccion direccion = beneficiario.getDireccion();
        if (direccion != null) {
            entity.setProvincia(direccion.provincia());
            entity.setCiudad(direccion.ciudad());
            entity.setCallePrincipal(direccion.calle());
            entity.setReferencia(direccion.referencia());
        }

        return entity;
    }

    public Beneficiario toDomain(BeneficiarioJpaEntity entity) {
        Direccion direccion = new Direccion(
                entity.getCallePrincipal(),
                entity.getCiudad(),
                entity.getProvincia(),
                entity.getReferencia(),
                null,
                null
        );

        return Beneficiario.reconstituir(
                entity.getId(),
                entity.getNombre(),
                entity.getRuc(),
                entity.getEmail(),
                entity.getTelefono(),
                direccion,
                entity.getEstadoVerificacion(),
                entity.getFechaRegistro()
        );
    }
}