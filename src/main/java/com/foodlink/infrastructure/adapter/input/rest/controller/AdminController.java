package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.AnalyticsDashboardResponse;
import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.port.input.AdministrarBeneficiarioUseCase;
import com.foodlink.domain.port.input.AdministrarComercioUseCase;
import com.foodlink.infrastructure.adapter.output.persistence.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Administración", description = "Gestión de actores y analytics. Requiere ROLE_ADMIN.")
public class AdminController {

    private final AdministrarComercioUseCase administrarComercioUseCase;
    private final AdministrarBeneficiarioUseCase administrarBeneficiarioUseCase;
    private final AnalyticsService analyticsService;

    public AdminController(
            AdministrarComercioUseCase administrarComercioUseCase,
            AdministrarBeneficiarioUseCase administrarBeneficiarioUseCase,
            AnalyticsService analyticsService) {
        this.administrarComercioUseCase = administrarComercioUseCase;
        this.administrarBeneficiarioUseCase = administrarBeneficiarioUseCase;
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Panel de analytics", description = "Métricas completas de la plataforma con top 5 comercios por impacto.")
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDashboardResponse> analytics() {
        return ResponseEntity.ok(analyticsService.obtenerAnalytics());
    }

    @Operation(summary = "Listar comercios pendientes de verificación")
    @GetMapping("/comercios/pendientes")
    public ResponseEntity<List<ComercioResponse>> comerciosPendientes() {
        return ResponseEntity.ok(administrarComercioUseCase.listarPendientes());
    }

    @Operation(summary = "Listar todos los comercios")
    @GetMapping("/comercios")
    public ResponseEntity<List<ComercioResponse>> todosLosComercios() {
        return ResponseEntity.ok(administrarComercioUseCase.listarTodos());
    }

    @Operation(summary = "Verificar comercio")
    @PostMapping("/comercios/{id}/verificar")
    public ResponseEntity<ComercioResponse> verificarComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.verificar(id));
    }

    @Operation(summary = "Rechazar comercio")
    @PostMapping("/comercios/{id}/rechazar")
    public ResponseEntity<ComercioResponse> rechazarComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.rechazar(id));
    }

    @Operation(summary = "Suspender comercio")
    @PostMapping("/comercios/{id}/suspender")
    public ResponseEntity<ComercioResponse> suspenderComercio(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarComercioUseCase.suspender(id));
    }

    @Operation(summary = "Listar beneficiarios pendientes")
    @GetMapping("/beneficiarios/pendientes")
    public ResponseEntity<List<BeneficiarioResponse>> beneficiariosPendientes() {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.listarPendientes());
    }

    @Operation(summary = "Listar todos los beneficiarios")
    @GetMapping("/beneficiarios")
    public ResponseEntity<List<BeneficiarioResponse>> todosLosBeneficiarios() {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.listarTodos());
    }

    @Operation(summary = "Verificar beneficiario")
    @PostMapping("/beneficiarios/{id}/verificar")
    public ResponseEntity<BeneficiarioResponse> verificarBeneficiario(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.verificar(id));
    }

    @Operation(summary = "Rechazar beneficiario")
    @PostMapping("/beneficiarios/{id}/rechazar")
    public ResponseEntity<BeneficiarioResponse> rechazarBeneficiario(@PathVariable UUID id) {
        return ResponseEntity.ok(administrarBeneficiarioUseCase.rechazar(id));
    }
}