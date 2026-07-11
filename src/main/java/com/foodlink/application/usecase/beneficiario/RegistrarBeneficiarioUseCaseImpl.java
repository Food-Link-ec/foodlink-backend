package com.foodlink.application.usecase.beneficiario;

import com.foodlink.application.dto.request.RegistrarBeneficiarioRequest;
import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.domain.event.BeneficiarioRegistrado;
import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.input.RegistrarBeneficiarioUseCase;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrarBeneficiarioUseCaseImpl implements RegistrarBeneficiarioUseCase {

    private final IRepositorioBeneficiario repositorioBeneficiario;
    private final ApplicationEventPublisher publicadorEventos;
    private final PasswordEncoder passwordEncoder;

    public RegistrarBeneficiarioUseCaseImpl(IRepositorioBeneficiario repositorioBeneficiario, ApplicationEventPublisher publicadorEventos,
                                             PasswordEncoder passwordEncoder) {
        this.repositorioBeneficiario = repositorioBeneficiario;
        this.publicadorEventos = publicadorEventos;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public BeneficiarioResponse registrar(RegistrarBeneficiarioRequest request) {
        if (repositorioBeneficiario.existePorRuc(request.ruc())) {
            throw new IllegalArgumentException("Ya existe un beneficiario registrado con ese RUC");
        }

        Direccion direccion = new Direccion(
                construirCalle(request.callePrincipal(), request.calleSecundaria()),
                request.ciudad(),
                request.provincia(),
                request.referencia(),
                null,
                null
        );

        Beneficiario beneficiario = Beneficiario.registrar(request.nombre(), request.ruc(), request.email(), request.telefono(), direccion);

        String passwordHash = passwordEncoder.encode(request.password());
        Beneficiario beneficiarioGuardado = repositorioBeneficiario.guardar(beneficiario, passwordHash);

        publicadorEventos.publishEvent(new BeneficiarioRegistrado(
                beneficiarioGuardado.getId(),
                beneficiarioGuardado.getRuc().valor(),
                beneficiarioGuardado.getNombre().valor(),
                LocalDateTime.now()
        ));

        return new BeneficiarioResponse(
                beneficiarioGuardado.getId(),
                beneficiarioGuardado.getNombre().valor(),
                beneficiarioGuardado.getRuc().valor(),
                beneficiarioGuardado.getEmail().valor(),
                beneficiarioGuardado.getEstadoVerificacion().name(),
                beneficiarioGuardado.getFechaRegistro()
        );
    }

    private String construirCalle(String callePrincipal, String calleSecundaria) {
        if (calleSecundaria == null || calleSecundaria.isBlank()) {
            return callePrincipal;
        }
        return callePrincipal + " y " + calleSecundaria;
    }
}