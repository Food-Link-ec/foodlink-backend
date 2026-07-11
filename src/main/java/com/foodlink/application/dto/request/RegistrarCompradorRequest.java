package com.foodlink.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarCompradorRequest(
        String cedula,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String password
) {
}
