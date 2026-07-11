package com.foodlink.application.usecase.auth;

import com.foodlink.application.dto.request.LoginRequest;
import com.foodlink.application.dto.response.AuthResponse;
import com.foodlink.domain.model.auth.exception.TokenInvalidoException;
import com.foodlink.domain.port.input.LoginUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.output.persistence.entity.RefreshTokenJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.RefreshTokenJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private static final String CREDENCIALES_INVALIDAS = "Credenciales inválidas";
    private static final String TOKEN_INVALIDO = "Token inválido o expirado";

    private final ComercioJpaRepository comercioJpaRepository;
    private final BeneficiarioJpaRepository beneficiarioJpaRepository;
    private final CompradorJpaRepository compradorJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public LoginUseCaseImpl(ComercioJpaRepository comercioJpaRepository,
                             BeneficiarioJpaRepository beneficiarioJpaRepository,
                             CompradorJpaRepository compradorJpaRepository,
                             RefreshTokenJpaRepository refreshTokenJpaRepository,
                             JwtService jwtService,
                             PasswordEncoder passwordEncoder,
                             @Value("${jwt.expiration-ms}") long expirationMs,
                             @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.comercioJpaRepository = comercioJpaRepository;
        this.beneficiarioJpaRepository = beneficiarioJpaRepository;
        this.compradorJpaRepository = compradorJpaRepository;
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Credenciales credenciales = buscarCredencialesPorEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(CREDENCIALES_INVALIDAS));

        if (!passwordEncoder.matches(request.password(), credenciales.passwordHash())) {
            throw new IllegalArgumentException(CREDENCIALES_INVALIDAS);
        }

        return generarAuthResponse(credenciales);
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.validarToken(refreshToken)) {
            throw new TokenInvalidoException(TOKEN_INVALIDO);
        }

        RefreshTokenJpaEntity guardado = refreshTokenJpaRepository.findByTokenHash(hashearToken(refreshToken))
                .orElseThrow(() -> new TokenInvalidoException(TOKEN_INVALIDO));

        if (guardado.getExpiraEn().isBefore(LocalDateTime.now())) {
            throw new TokenInvalidoException(TOKEN_INVALIDO);
        }

        Credenciales credenciales = buscarCredencialesPorId(guardado.getUsuarioId(), guardado.getTipoUsuario())
                .orElseThrow(() -> new TokenInvalidoException(TOKEN_INVALIDO));

        String nuevoAccessToken = jwtService.generarAccessToken(
                credenciales.usuarioId(), credenciales.email(), credenciales.rol(), credenciales.tipoUsuario());

        return new AuthResponse(nuevoAccessToken, refreshToken, credenciales.tipoUsuario(),
                credenciales.usuarioId(), credenciales.email(), credenciales.rol(), credenciales.nombre(), expirationMs);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenJpaRepository.findByTokenHash(hashearToken(refreshToken))
                .ifPresent(refreshTokenJpaRepository::delete);
    }

    private AuthResponse generarAuthResponse(Credenciales credenciales) {
        String accessToken = jwtService.generarAccessToken(
                credenciales.usuarioId(), credenciales.email(), credenciales.rol(), credenciales.tipoUsuario());
        String refreshToken = jwtService.generarRefreshToken(credenciales.usuarioId());

        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsuarioId(credenciales.usuarioId());
        entity.setTipoUsuario(credenciales.tipoUsuario());
        entity.setTokenHash(hashearToken(refreshToken));
        entity.setExpiraEn(LocalDateTime.now().plus(Duration.ofMillis(refreshExpirationMs)));
        entity.setCreadoEn(LocalDateTime.now());
        refreshTokenJpaRepository.save(entity);

        return new AuthResponse(accessToken, refreshToken, credenciales.tipoUsuario(),
                credenciales.usuarioId(), credenciales.email(), credenciales.rol(), credenciales.nombre(), expirationMs);
    }

    private Optional<Credenciales> buscarCredencialesPorEmail(String email) {
        Optional<Credenciales> comercio = comercioJpaRepository.findByEmail(email)
                .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                        "ROLE_COMERCIO", "COMERCIO", entity.getNombre()));
        if (comercio.isPresent()) {
            return comercio;
        }

        Optional<Credenciales> beneficiario = beneficiarioJpaRepository.findByEmail(email)
                .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                        "ROLE_BENEFICIARIO", "BENEFICIARIO", entity.getNombre()));
        if (beneficiario.isPresent()) {
            return beneficiario;
        }

        return compradorJpaRepository.findByEmail(email)
                .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                        "ROLE_COMPRADOR", "COMPRADOR", entity.getNombre() + " " + entity.getApellido()));
    }

    private Optional<Credenciales> buscarCredencialesPorId(UUID usuarioId, String tipoUsuario) {
        return switch (tipoUsuario) {
            case "COMERCIO" -> comercioJpaRepository.findById(usuarioId)
                    .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                            "ROLE_COMERCIO", "COMERCIO", entity.getNombre()));
            case "BENEFICIARIO" -> beneficiarioJpaRepository.findById(usuarioId)
                    .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                            "ROLE_BENEFICIARIO", "BENEFICIARIO", entity.getNombre()));
            case "COMPRADOR" -> compradorJpaRepository.findById(usuarioId)
                    .map(entity -> new Credenciales(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                            "ROLE_COMPRADOR", "COMPRADOR", entity.getNombre() + " " + entity.getApellido()));
            default -> Optional.empty();
        };
    }

    private String hashearToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Algoritmo de hash no disponible", ex);
        }
    }

    private record Credenciales(UUID usuarioId, String email, String passwordHash, String rol, String tipoUsuario, String nombre) {
    }
}