package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.port.input.AdministrarBeneficiarioUseCase;
import com.foodlink.domain.port.input.AdministrarComercioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdministrarComercioUseCase administrarComercioUseCase;
    private final AdministrarBeneficiarioUseCase administrarBeneficiarioUseCase;

    public AdminController(
            AdministrarComercioUseCase administrarComercioUseCase,
            AdministrarBeneficiarioUseCase administrarBeneficiarioUseCase) {
        this.administrarComercioUseCase = administrarComercioUseCase;
        this.administrarBeneficiarioUseCase = administrarBeneficiarioUseCase;
    }

    @GetMapping("/comercios/pendientes")
    public ResponseEntity<List<ComercioResponse>> comerciosPendientes() {
        return ResponseEntity.ok(administrarComercioUseCase.listarPendientes());
    }

    @GetMapping("/comercios")
    public ResponseEntity<List<ComercioResponse>> todosLosComercios() {
        return ResponseEntity.ok(administrarComercioUseCase.listarTodos());
    }

    @PostMapping("/comercios/{id}/verificar")
    public ResponseEntity<ComercioResponse> verificarComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.verificar(id));
    }

    @PostMapping("/comercios/{id}/rechazar")
    public ResponseEntity<ComercioResponse> rechazarComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.rechazar(id));
    }

    @PostMapping("/comercios/{id}/suspender")
    public ResponseEntity<ComercioResponse> suspenderComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.suspender(id));
    }

    @GetMapping("/beneficiarios/pendientes")
    public ResponseEntity<List<BeneficiarioResponse>> beneficiariosPendientes() {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.listarPendientes());
    }

    @GetMapping("/beneficiarios")
    public ResponseEntity<List<BeneficiarioResponse>> todosLosBeneficiarios() {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.listarTodos());
    }

    @PostMapping("/beneficiarios/{id}/verificar")
    public ResponseEntity<BeneficiarioResponse> verificarBeneficiario(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.verificar(id));
    }

    @PostMapping("/beneficiarios/{id}/rechazar")
    public ResponseEntity<BeneficiarioResponse> rechazarBeneficiario(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.rechazar(id));
    }
}