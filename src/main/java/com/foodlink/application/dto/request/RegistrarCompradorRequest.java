package com.foodlink.application.dto.request;

public record RegistrarCompradorRequest(
        String cedula,
        String nombre,
        String apellido,
        String email,
        String telefono
) {
}