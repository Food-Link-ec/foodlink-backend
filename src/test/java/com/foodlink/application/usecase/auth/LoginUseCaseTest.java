package com.foodlink.application.usecase.auth;

import com.foodlink.application.dto.request.LoginRequest;
import com.foodlink.application.dto.response.AuthResponse;
import com.foodlink.domain.model.auth.exception.TokenInvalidoException;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.output.persistence.entity.BeneficiarioJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ComercioJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.entity.CompradorJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.entity.RefreshTokenJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.AdministradorJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.RefreshTokenJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    private static final String EMAIL = "usuario@mail.com";
    private static final String PASSWORD = "SuperClave123";
    private static final String PASSWORD_HASH = "hash-almacenado";

    @Mock
    private AdministradorJpaRepository administradorJpaRepository;

    @Mock
    private ComercioJpaRepository comercioJpaRepository;

    @Mock
    private BeneficiarioJpaRepository beneficiarioJpaRepository;

    @Mock
    private CompradorJpaRepository compradorJpaRepository;

    @Mock
    private RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private LoginUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCaseImpl(administradorJpaRepository, comercioJpaRepository, beneficiarioJpaRepository,
                compradorJpaRepository, refreshTokenJpaRepository, jwtService, passwordEncoder, 3600000L, 604800000L);
    }

    private LoginRequest requestValido() {
        return new LoginRequest(EMAIL, PASSWORD);
    }

    @Test
    void loginExitosoConComercioDeberiaRetornarAuthResponseConRoleComercio() {
        UUID usuarioId = UUID.randomUUID();
        ComercioJpaEntity entity = new ComercioJpaEntity();
        entity.setId(usuarioId);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);

        when(comercioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(PASSWORD, PASSWORD_HASH)).thenReturn(true);
        when(jwtService.generarAccessToken(eq(usuarioId), eq(EMAIL), eq("ROLE_COMERCIO"), eq("COMERCIO")))
                .thenReturn("access-token");
        when(jwtService.generarRefreshToken(usuarioId)).thenReturn("refresh-token");

        AuthResponse response = useCase.login(requestValido());

        assertEquals("ROLE_COMERCIO", response.rol());
        assertEquals("COMERCIO", response.tipoUsuario());
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void loginExitosoConBeneficiarioDeberiaRetornarAuthResponseConRoleBeneficiario() {
        UUID usuarioId = UUID.randomUUID();
        BeneficiarioJpaEntity entity = new BeneficiarioJpaEntity();
        entity.setId(usuarioId);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);

        when(comercioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(beneficiarioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(PASSWORD, PASSWORD_HASH)).thenReturn(true);
        when(jwtService.generarAccessToken(eq(usuarioId), eq(EMAIL), eq("ROLE_BENEFICIARIO"), eq("BENEFICIARIO")))
                .thenReturn("access-token");
        when(jwtService.generarRefreshToken(usuarioId)).thenReturn("refresh-token");

        AuthResponse response = useCase.login(requestValido());

        assertEquals("ROLE_BENEFICIARIO", response.rol());
        assertEquals("BENEFICIARIO", response.tipoUsuario());
    }

    @Test
    void loginExitosoConCompradorDeberiaRetornarAuthResponseConRoleComprador() {
        UUID usuarioId = UUID.randomUUID();
        CompradorJpaEntity entity = new CompradorJpaEntity();
        entity.setId(usuarioId);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);

        when(comercioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(beneficiarioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(compradorJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(PASSWORD, PASSWORD_HASH)).thenReturn(true);
        when(jwtService.generarAccessToken(eq(usuarioId), eq(EMAIL), eq("ROLE_COMPRADOR"), eq("COMPRADOR")))
                .thenReturn("access-token");
        when(jwtService.generarRefreshToken(usuarioId)).thenReturn("refresh-token");

        AuthResponse response = useCase.login(requestValido());

        assertEquals("ROLE_COMPRADOR", response.rol());
        assertEquals("COMPRADOR", response.tipoUsuario());
    }

    @Test
    void loginConEmailNoEncontradoDeberiaLanzarCredencialesInvalidas() {
        when(comercioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(beneficiarioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(compradorJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> useCase.login(requestValido()));

        assertEquals("Credenciales inválidas", excepcion.getMessage());
    }

    @Test
    void loginConPasswordIncorrectaDeberiaLanzarCredencialesInvalidas() {
        UUID usuarioId = UUID.randomUUID();
        ComercioJpaEntity entity = new ComercioJpaEntity();
        entity.setId(usuarioId);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);

        when(comercioJpaRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(PASSWORD, PASSWORD_HASH)).thenReturn(false);

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> useCase.login(requestValido()));

        assertEquals("Credenciales inválidas", excepcion.getMessage());
    }

    @Test
    void refreshConTokenValidoDeberiaRetornarNuevoAccessToken() {
        UUID usuarioId = UUID.randomUUID();
        String refreshToken = "refresh-token-valido";

        RefreshTokenJpaEntity guardado = new RefreshTokenJpaEntity();
        guardado.setId(UUID.randomUUID());
        guardado.setUsuarioId(usuarioId);
        guardado.setTipoUsuario("COMERCIO");
        guardado.setExpiraEn(LocalDateTime.now().plusDays(1));

        ComercioJpaEntity entity = new ComercioJpaEntity();
        entity.setId(usuarioId);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);

        when(jwtService.validarToken(refreshToken)).thenReturn(true);
        when(refreshTokenJpaRepository.findByTokenHash(anyString())).thenReturn(Optional.of(guardado));
        when(comercioJpaRepository.findById(usuarioId)).thenReturn(Optional.of(entity));
        when(jwtService.generarAccessToken(eq(usuarioId), eq(EMAIL), eq("ROLE_COMERCIO"), eq("COMERCIO")))
                .thenReturn("nuevo-access-token");

        AuthResponse response = useCase.refresh(refreshToken);

        assertEquals("nuevo-access-token", response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
    }

    @Test
    void refreshConTokenNoEncontradoEnBdDeberiaLanzarTokenInvalido() {
        String refreshToken = "refresh-token-desconocido";

        when(jwtService.validarToken(refreshToken)).thenReturn(true);
        when(refreshTokenJpaRepository.findByTokenHash(anyString())).thenReturn(Optional.empty());

        assertThrows(TokenInvalidoException.class, () -> useCase.refresh(refreshToken));
    }
}
