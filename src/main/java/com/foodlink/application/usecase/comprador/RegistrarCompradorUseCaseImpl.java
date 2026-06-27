package com.foodlink.application.usecase.comprador;

import com.foodlink.application.dto.request.RegistrarCompradorRequest;
import com.foodlink.application.dto.response.CompradorResponse;
import com.foodlink.domain.event.CompradorRegistrado;
import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.domain.port.input.RegistrarCompradorUseCase;
import com.foodlink.domain.port.output.IRepositorioComprador;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrarCompradorUseCaseImpl implements RegistrarCompradorUseCase {

    private final IRepositorioComprador repositorioComprador;
    private final ApplicationEventPublisher publicadorEventos;

    public RegistrarCompradorUseCaseImpl(IRepositorioComprador repositorioComprador, ApplicationEventPublisher publicadorEventos) {
        this.repositorioComprador = repositorioComprador;
        this.publicadorEventos = publicadorEventos;
    }

    @Override
    public CompradorResponse registrar(RegistrarCompradorRequest request) {
        if (repositorioComprador.existePorCedula(request.cedula())) {
            throw new IllegalArgumentException("Ya existe un comprador registrado con esa cédula");
        }

        Comprador comprador = Comprador.registrar(request.cedula(), request.nombre(), request.apellido(), request.email(), request.telefono());

        Comprador compradorGuardado = repositorioComprador.guardar(comprador);

        publicadorEventos.publishEvent(new CompradorRegistrado(
                compradorGuardado.getId(),
                compradorGuardado.getCedula(),
                compradorGuardado.getNombre(),
                LocalDateTime.now()
        ));

        return new CompradorResponse(
                compradorGuardado.getId(),
                compradorGuardado.getCedula(),
                compradorGuardado.getNombre(),
                compradorGuardado.getApellido(),
                compradorGuardado.getEmail(),
                compradorGuardado.estaActivo(),
                compradorGuardado.getFechaRegistro()
        );
    }
}