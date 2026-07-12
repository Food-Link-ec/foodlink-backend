package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlink.application.dto.request.LoginRequest;
import com.foodlink.application.dto.request.RefreshTokenRequest;
import com.foodlink.application.dto.response.AuthResponse;
import com.foodlink.domain.port.input.LoginUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private JwtService jwtService;

    private AuthResponse authResponseValido() {
        return new AuthResponse("access-token", "refresh-token", "COMERCIO",
                UUID.randomUUID(), "comercio@test.com", "ROLE_COMERCIO", "Supermaxi", 3600000L);
    }

    @Test
    void loginConCredencialesValidasDeberiaRetornar200ConAccessToken() throws Exception {
        when(loginUseCase.login(any())).thenReturn(authResponseValido());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("comercio@test.com", "Clave123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }

    @Test
    void loginConBodyVacioDeberiaRetornar400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshConRefreshTokenValidoDeberiaRetornar200() throws Exception {
        when(loginUseCase.refresh(anyString())).thenReturn(authResponseValido());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest("refresh-token-valido"))))
                .andExpect(status().isOk());
    }

    @Test
    void logoutAutenticadoDeberiaRetornar204() throws Exception {
        UsuarioAutenticado usuario = UsuarioAutenticado.of(UUID.randomUUID(), "comercio@test.com", "ROLE_COMERCIO", "COMERCIO");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(authentication(usuario))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest("refresh-token-valido"))))
                .andExpect(status().isNoContent());
    }
}
