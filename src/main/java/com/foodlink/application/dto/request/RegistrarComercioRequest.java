package com.foodlink.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarComercioRequest(
        @NotBlank String ruc,
        @NotBlank @Size(max = 200) String nombre,
        @NotBlank String telefono,
        @NotBlank @Email String email,
        @NotBlank String provincia,
        @NotBlank String ciudad,
        @NotBlank String callePrincipal,
        String calleSecundaria,
        String referencia,
        String password
) {
}
