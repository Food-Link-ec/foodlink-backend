package com.foodlink.application.dto.request;

public record RegistrarBeneficiarioRequest(
        String nombre,
        String ruc,
        String email,
        String telefono,
        String provincia,
        String ciudad,
        String callePrincipal,
        String calleSecundaria,
        String referencia,
        String password
) {
}