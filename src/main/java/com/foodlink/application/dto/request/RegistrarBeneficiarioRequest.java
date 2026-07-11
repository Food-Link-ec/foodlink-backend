package com.foodlink.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarBeneficiarioRequest(
        @NotBlank @Size(max = 200) String nombre,
        @NotBlank String ruc,
        @NotBlank @Email String email,
        @NotBlank String telefono,
        @NotBlank String provincia,
        @NotBlank String ciudad,
        @NotBlank String callePrincipal,
        String calleSecundaria,
        String referencia,
        String password
) {
}
