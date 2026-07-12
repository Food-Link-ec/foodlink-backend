package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.MiPerfilResponse;
import com.foodlink.domain.port.input.MiPerfilUseCase;
import com.foodlink.domain.port.input.MisLotesUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerfilController.class)
@Import(SecurityConfig.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MiPerfilUseCase miPerfilUseCase;

    @MockBean
    private MisLotesUseCase misLotesUseCase;

    @MockBean
    private JwtService jwtService;

    private UsuarioAutenticado usuarioAutenticado() {
        return UsuarioAutenticado.of(UUID.randomUUID(), "comercio@test.com", "ROLE_COMERCIO", "COMERCIO");
    }

    @Test
    void miPerfilAutenticadoDeberiaRetornar200ConMiPerfilResponse() throws Exception {
        UUID id = UUID.randomUUID();
        when(miPerfilUseCase.obtenerMiPerfil(any(), any())).thenReturn(
                new MiPerfilResponse(id, "comercio@test.com", "COMERCIO", "ROLE_COMERCIO", null));

        mockMvc.perform(get("/api/v1/auth/me")
                        .with(authentication(usuarioAutenticado())))
                .andExpect(status().isOk());
    }

    @Test
    void miPerfilSinAutenticacionDeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void misLotesAutenticadoDeberiaRetornar200() throws Exception {
        when(misLotesUseCase.obtenerMisLotes(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/comercios/mis-lotes")
                        .with(authentication(usuarioAutenticado())))
                .andExpect(status().isOk());
    }

    @Test
    void misReservasAutenticadoDeberiaRetornar200() throws Exception {
        when(misLotesUseCase.obtenerMisReservas(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/redistribucion/mis-reservas")
                        .with(authentication(usuarioAutenticado())))
                .andExpect(status().isOk());
    }
}
