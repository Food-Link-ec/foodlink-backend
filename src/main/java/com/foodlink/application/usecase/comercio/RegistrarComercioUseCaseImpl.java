package com.foodlink.application.usecase.comercio;

import com.foodlink.application.dto.request.RegistrarComercioRequest;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.event.ComercioRegistrado;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.input.RegistrarComercioUseCase;
import com.foodlink.domain.port.output.IRepositorioComercio;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrarComercioUseCaseImpl implements RegistrarComercioUseCase {

    private final IRepositorioComercio repositorioComercio;
    private final ApplicationEventPublisher publicadorEventos;
    private final PasswordEncoder passwordEncoder;

    public RegistrarComercioUseCaseImpl(IRepositorioComercio repositorioComercio, ApplicationEventPublisher publicadorEventos,
                                         PasswordEncoder passwordEncoder) {
        this.repositorioComercio = repositorioComercio;
        this.publicadorEventos = publicadorEventos;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ComercioResponse registrar(RegistrarComercioRequest request) {
        if (repositorioComercio.existePorRuc(request.ruc())) {
            throw new IllegalArgumentException("Ya existe un comercio registrado con ese RUC");
        }

        Direccion direccion = new Direccion(
                construirCalle(request.callePrincipal(), request.calleSecundaria()),
                request.ciudad(),
                request.provincia(),
                request.referencia(),
                null,
                null
        );

        Comercio comercio = Comercio.crear(request.ruc(), request.nombre(), request.telefono(), request.email(), direccion);

        String passwordHash = passwordEncoder.encode(request.password());
        Comercio comercioGuardado = repositorioComercio.guardar(comercio, passwordHash);

        publicadorEventos.publishEvent(new ComercioRegistrado(
                comercioGuardado.getId(),
                comercioGuardado.getRuc(),
                comercioGuardado.getNombre(),
                LocalDateTime.now()
        ));

        return new ComercioResponse(
                comercioGuardado.getId(),
                comercioGuardado.getRuc(),
                comercioGuardado.getNombre(),
                comercioGuardado.getTelefono(),
                comercioGuardado.getEmail(),
                comercioGuardado.getEstado().name(),
                comercioGuardado.getFechaRegistro()
        );
    }

    private String construirCalle(String callePrincipal, String calleSecundaria) {
        if (calleSecundaria == null || calleSecundaria.isBlank()) {
            return callePrincipal;
        }
        return callePrincipal + " y " + calleSecundaria;
    }
}