package com.foodlink.application.usecase.admin;

import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.domain.port.input.AdministrarComercioUseCase;
import com.foodlink.domain.port.output.IRepositorioComercio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdministrarComercioUseCaseImpl implements AdministrarComercioUseCase {

    private final IRepositorioComercio repositorioComercio;

    public AdministrarComercioUseCaseImpl(IRepositorioComercio repositorioComercio) {
        this.repositorioComercio = repositorioComercio;
    }

    @Override
    public ComercioResponse verificar(UUID comercioId) {
        Comercio comercio = repositorioComercio.buscarPorId(comercioId)
                .orElseThrow(() -> new IllegalArgumentException("Comercio no encontrado"));
        comercio.verificar();
        return toResponse(repositorioComercio.guardar(comercio));
    }

    @Override
    public ComercioResponse rechazar(UUID comercioId) {
        Comercio comercio = repositorioComercio.buscarPorId(comercioId)
                .orElseThrow(() -> new IllegalArgumentException("Comercio no encontrado"));
        comercio.rechazar("Rechazado por el administrador");
        return toResponse(repositorioComercio.guardar(comercio));
    }

    @Override
    public ComercioResponse suspender(UUID comercioId) {
        Comercio comercio = repositorioComercio.buscarPorId(comercioId)
                .orElseThrow(() -> new IllegalArgumentException("Comercio no encontrado"));
        comercio.suspender();
        return toResponse(repositorioComercio.guardar(comercio));
    }

    @Override
    public List<ComercioResponse> listarPendientes() {
        return repositorioComercio.buscarPorEstado(EstadoComercio.PENDIENTE_VERIFICACION)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComercioResponse> listarTodos() {
        return repositorioComercio.buscarTodos()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ComercioResponse toResponse(Comercio comercio) {
        return new ComercioResponse(
                comercio.getId(),
                comercio.getRuc().valor(),
                comercio.getNombre().valor(),
                comercio.getTelefono().valor(),
                comercio.getEmail().valor(),
                comercio.getEstado().name(),
                comercio.getFechaRegistro()
        );
    }
}