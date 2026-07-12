package com.foodlink.application.usecase.admin;

import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.beneficiario.EstadoVerificacion;
import com.foodlink.domain.port.input.AdministrarBeneficiarioUseCase;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdministrarBeneficiarioUseCaseImpl implements AdministrarBeneficiarioUseCase {

    private final IRepositorioBeneficiario repositorioBeneficiario;

    public AdministrarBeneficiarioUseCaseImpl(IRepositorioBeneficiario repositorioBeneficiario) {
        this.repositorioBeneficiario = repositorioBeneficiario;
    }

    @Override
    public BeneficiarioResponse verificar(UUID beneficiarioId) {
        Beneficiario beneficiario = repositorioBeneficiario.buscarPorId(beneficiarioId)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
        beneficiario.verificar();
        return toResponse(repositorioBeneficiario.guardar(beneficiario));
    }

    @Override
    public BeneficiarioResponse rechazar(UUID beneficiarioId) {
        Beneficiario beneficiario = repositorioBeneficiario.buscarPorId(beneficiarioId)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
        beneficiario.rechazar("Rechazado por el administrador");
        return toResponse(repositorioBeneficiario.guardar(beneficiario));
    }

    @Override
    public List<BeneficiarioResponse> listarPendientes() {
        return repositorioBeneficiario.buscarPorEstado(EstadoVerificacion.PENDIENTE)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeneficiarioResponse> listarTodos() {
        return repositorioBeneficiario.buscarTodos()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BeneficiarioResponse toResponse(Beneficiario beneficiario) {
        return new BeneficiarioResponse(
                beneficiario.getId(),
                beneficiario.getNombre().valor(),
                beneficiario.getRuc().valor(),
                beneficiario.getEmail().valor(),
                beneficiario.getEstadoVerificacion().name(),
                beneficiario.getFechaRegistro()
        );
    }
}