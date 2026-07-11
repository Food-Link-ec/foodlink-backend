package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.ValidarPinRequest;
import com.foodlink.application.dto.response.PinRetiroResponse;
import com.foodlink.domain.port.input.GenerarPinUseCase;
import com.foodlink.domain.port.input.ValidarPinUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
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
public class PinRetiroController {

    private final GenerarPinUseCase generarPinUseCase;
    private final ValidarPinUseCase validarPinUseCase;

    public PinRetiroController(
            GenerarPinUseCase generarPinUseCase,
            ValidarPinUseCase validarPinUseCase) {
        this.generarPinUseCase = generarPinUseCase;
        this.validarPinUseCase = validarPinUseCase;
    }

    @PostMapping("/pin/{loteId}")
    public ResponseEntity<PinRetiroResponse> generarPin(
            @PathVariable UUID loteId) {
        return ResponseEntity.ok(generarPinUseCase.generarPin(loteId));
    }

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