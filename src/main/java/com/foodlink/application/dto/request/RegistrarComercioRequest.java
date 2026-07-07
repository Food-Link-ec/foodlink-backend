package com.foodlink.application.dto.request;

public record RegistrarComercioRequest(
        String ruc,
        String nombre,
        String telefono,
        String email,
        String provincia,
        String ciudad,
        String callePrincipal,
        String calleSecundaria,
        String referencia,
        String password
) {
}