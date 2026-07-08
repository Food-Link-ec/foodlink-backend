package com.foodlink.infrastructure.adapter.input.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(PREFIJO_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIJO_BEARER.length());
        if (!jwtService.validarToken(token)) {
            responderNoAutorizado(response);
            return;
        }

        Claims claims = jwtService.extraerClaims(token);
        UUID usuarioId = UUID.fromString(claims.getSubject());
        String email = claims.get("email", String.class);
        String rol = claims.get("rol", String.class);
        String tipoUsuario = claims.get("tipoUsuario", String.class);

        SecurityContextHolder.getContext().setAuthentication(UsuarioAutenticado.of(usuarioId, email, rol, tipoUsuario));
        filterChain.doFilter(request, response);
    }

    private void responderNoAutorizado(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> cuerpo = Map.of(
                "status", 401,
                "error", "UNAUTHORIZED",
                "mensaje", "Token inválido o expirado"
        );
        response.getWriter().write(objectMapper.writeValueAsString(cuerpo));
    }
}
