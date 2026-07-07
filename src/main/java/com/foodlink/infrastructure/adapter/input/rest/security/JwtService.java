package com.foodlink.infrastructure.adapter.input.rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.expiration-ms}") long expirationMs,
                       @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generarAccessToken(UUID usuarioId, String email, String rol, String tipoUsuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);
        return Jwts.builder()
                .subject(usuarioId.toString())
                .claim("email", email)
                .claim("rol", rol)
                .claim("tipoUsuario", tipoUsuario)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();
    }

    public String generarRefreshToken(UUID usuarioId) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + refreshExpirationMs);
        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public UUID extraerUsuarioId(String token) {
        return UUID.fromString(extraerClaims(token).getSubject());
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
