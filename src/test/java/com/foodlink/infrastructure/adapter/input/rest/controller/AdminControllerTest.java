package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.port.input.AdministrarBeneficiarioUseCase;
import com.foodlink.domain.port.input.AdministrarComercioUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministrarComercioUseCase administrarComercioUseCase;

    @MockBean
    private AdministrarBeneficiarioUseCase administrarBeneficiarioUseCase;

    @MockBean
    private JwtService jwtService;

    private UsuarioAutenticado usuarioAdmin() {
        return UsuarioAutenticado.of(UUID.randomUUID(), "admin@foodlink.ec", "ROLE_ADMIN", "ADMIN");
    }

    private UsuarioAutenticado usuarioComercio() {
        return UsuarioAutenticado.of(UUID.randomUUID(), "comercio@test.com", "ROLE_COMERCIO", "COMERCIO");
    }

    private ComercioResponse comercioResponse() {
        return new ComercioResponse(UUID.randomUUID(), "1791000005001", "Supermaxi", "0991234567",
                "comercio@test.com", "VERIFICADO", LocalDateTime.now());
    }

    private BeneficiarioResponse beneficiarioResponse() {
        return new BeneficiarioResponse(UUID.randomUUID(), "Fundación Manos Unidas", "1791000048001",
                "beneficiario@test.com", "VERIFICADO", LocalDateTime.now());
    }

    @Test
    void comerciosPendientesConRolAdminDeberiaRetornar200() throws Exception {
        when(administrarComercioUseCase.listarPendientes()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/comercios/pendientes")
                        .with(authentication(usuarioAdmin())))
                .andExpect(status().isOk());
    }

    @Test
    void comerciosPendientesSinAutenticacionDeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/comercios/pendientes"))
                .andExpect(status().isForbidden());
    }

    @Test
    void comerciosPendientesConRolComercioDeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/comercios/pendientes")
                        .with(authentication(usuarioComercio())))
                .andExpect(status().isForbidden());
    }

    @Test
    void verificarComercioConRolAdminDeberiaRetornar200() throws Exception {
        when(administrarComercioUseCase.verificar(any())).thenReturn(comercioResponse());

        mockMvc.perform(post("/api/v1/admin/comercios/{id}/verificar", UUID.randomUUID())
                        .with(authentication(usuarioAdmin())))
                .andExpect(status().isOk());
    }

    @Test
    void rechazarComercioConRolAdminDeberiaRetornar200() throws Exception {
        when(administrarComercioUseCase.rechazar(any())).thenReturn(comercioResponse());

        mockMvc.perform(post("/api/v1/admin/comercios/{id}/rechazar", UUID.randomUUID())
                        .with(authentication(usuarioAdmin())))
                .andExpect(status().isOk());
    }

    @Test
    void beneficiariosPendientesConRolAdminDeberiaRetornar200() throws Exception {
        when(administrarBeneficiarioUseCase.listarPendientes()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/beneficiarios/pendientes")
                        .with(authentication(usuarioAdmin())))
                .andExpect(status().isOk());
    }

    @Test
    void verificarBeneficiarioConRolAdminDeberiaRetornar200() throws Exception {
        when(administrarBeneficiarioUseCase.verificar(any())).thenReturn(beneficiarioResponse());

        mockMvc.perform(post("/api/v1/admin/beneficiarios/{id}/verificar", UUID.randomUUID())
                        .with(authentication(usuarioAdmin())))
                .andExpect(status().isOk());
    }
}
