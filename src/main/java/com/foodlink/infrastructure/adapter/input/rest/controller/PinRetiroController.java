package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.ValidarPinRequest;
import com.foodlink.application.dto.response.PinRetiroResponse;
import com.foodlink.domain.port.input.GenerarPinUseCase;
import com.foodlink.domain.port.input.ValidarPinUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/retiros")
@Tag(name = "Retiro", description = "Generación y validación de PIN de retiro")
public class PinRetiroController {

    private final GenerarPinUseCase generarPinUseCase;
    private final ValidarPinUseCase validarPinUseCase;

    public PinRetiroController(
            GenerarPinUseCase generarPinUseCase,
            ValidarPinUseCase validarPinUseCase) {
        this.generarPinUseCase = generarPinUseCase;
        this.validarPinUseCase = validarPinUseCase;
    }

    @Operation(summary = "Generar PIN de retiro", description = "Genera PIN de 5 caracteres y qrData para confirmar retiro físico.")
    @PostMapping("/pin/{loteId}")
    public ResponseEntity<PinRetiroResponse> generarPin(
            @PathVariable UUID loteId) {
        return ResponseEntity.ok(generarPinUseCase.generarPin(loteId));
    }

    @Operation(summary = "Validar PIN", description = "Lote pasa a ENTREGADO. Registra impacto automáticamente.")
    @PostMapping("/validar")
    public ResponseEntity<Map<String, String>> validarPin(
            @RequestBody ValidarPinRequest request,
            @CurrentUser UsuarioAutenticado usuario) {
        validarPinUseCase.validarPin(request, usuario.getUsuarioId());
        return ResponseEntity.ok(Map.of(
                "mensaje", "Retiro confirmado exitosamente",
                "loteId", request.loteId().toString()
        ));
    }
}