package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.RegistrarComercioRequest;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.application.dto.response.DashboardComercioResponse;
import com.foodlink.domain.port.input.RegistrarComercioUseCase;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.adapter.output.persistence.ComercioStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comercios")
@Tag(name = "Registro", description = "Registro de nuevos actores en la plataforma")
public class ComercioController {

    private final RegistrarComercioUseCase registrarComercioUseCase;
    private final ComercioStatsService comercioStatsService;
    private final IRepositorioComercio repositorioComercio;

    public ComercioController(RegistrarComercioUseCase registrarComercioUseCase,
                               ComercioStatsService comercioStatsService,
                               IRepositorioComercio repositorioComercio) {
        this.registrarComercioUseCase = registrarComercioUseCase;
        this.comercioStatsService = comercioStatsService;
        this.repositorioComercio = repositorioComercio;
    }

    @Operation(summary = "Registrar nuevo comercio")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ComercioResponse> registrar(@Valid @RequestBody RegistrarComercioRequest request) {
        ComercioResponse response = registrarComercioUseCase.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Dashboard del comercio autenticado")
    @GetMapping("/mi-dashboard")
    public ResponseEntity<DashboardComercioResponse> miDashboard(@CurrentUser UsuarioAutenticado usuario) {
        String nombre = repositorioComercio.buscarPorId(usuario.getUsuarioId())
                .map(c -> c.getNombre().valor())
                .orElse("Comercio");
        return ResponseEntity.ok(comercioStatsService.obtenerDashboard(usuario.getUsuarioId(), nombre));
    }
}
