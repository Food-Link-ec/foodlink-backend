package com.foodlink.infrastructure.adapter.input.rest.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String SECRETO = "clave-de-prueba-con-al-menos-256-bits-de-longitud-1234567890";

    private final JwtService jwtService = new JwtService(SECRETO, 3600000L, 604800000L);

    @Test
    void generarAccessTokenDeberiaProducirTokenConClaimsCorrectos() {
        UUID usuarioId = UUID.randomUUID();

        String token = jwtService.generarAccessToken(usuarioId, "usuario@mail.com", "ROLE_COMERCIO", "COMERCIO");

        Claims claims = jwtService.extraerClaims(token);
        assertEquals(usuarioId.toString(), claims.getSubject());
        assertEquals("usuario@mail.com", claims.get("email", String.class));
        assertEquals("ROLE_COMERCIO", claims.get("rol", String.class));
        assertEquals("COMERCIO", claims.get("tipoUsuario", String.class));
    }

    @Test
    void validarTokenConTokenValidoDeberiaRetornarTrue() {
        String token = jwtService.generarAccessToken(UUID.randomUUID(), "usuario@mail.com", "ROLE_COMERCIO", "COMERCIO");

        assertTrue(jwtService.validarToken(token));
    }

    @Test
    void validarTokenConTokenExpiradoDeberiaRetornarFalse() {
        JwtService jwtServiceExpirado = new JwtService(SECRETO, -1000L, 604800000L);
        String token = jwtServiceExpirado.generarAccessToken(UUID.randomUUID(), "usuario@mail.com", "ROLE_COMERCIO", "COMERCIO");

        assertFalse(jwtService.validarToken(token));
    }

    @Test
    void validarTokenConFirmaIncorrectaDeberiaRetornarFalse() {
        JwtService jwtServiceOtraClave = new JwtService(
                "otra-clave-de-prueba-con-al-menos-256-bits-0987654321", 3600000L, 604800000L);
        String token = jwtServiceOtraClave.generarAccessToken(UUID.randomUUID(), "usuario@mail.com", "ROLE_COMERCIO", "COMERCIO");

        assertFalse(jwtService.validarToken(token));
    }

    @Test
    void extraerUsuarioIdDeberiaRetornarElUuidCorrecto() {
        UUID usuarioId = UUID.randomUUID();
        String token = jwtService.generarAccessToken(usuarioId, "usuario@mail.com", "ROLE_COMERCIO", "COMERCIO");

        assertEquals(usuarioId, jwtService.extraerUsuarioId(token));
    }
}
