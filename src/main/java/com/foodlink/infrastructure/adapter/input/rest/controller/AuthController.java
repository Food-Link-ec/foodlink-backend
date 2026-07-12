package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.LoginRequest;
import com.foodlink.application.dto.request.RefreshTokenRequest;
import com.foodlink.application.dto.response.AuthResponse;
import com.foodlink.domain.port.input.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Autenticación y gestión de tokens JWT")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Operation(summary = "Iniciar sesión", description = "Retorna accessToken y refreshToken. Funciona para Admin, Comercio, Beneficiario y Comprador.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.login(request));
    }

    @Operation(summary = "Renovar token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(loginUseCase.refresh(request.refreshToken()));
    }

    @Operation(summary = "Cerrar sesión")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        loginUseCase.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
