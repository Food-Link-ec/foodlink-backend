package com.foodlink.application.usecase.beneficiario;

import com.foodlink.application.dto.request.RegistrarBeneficiarioRequest;
import com.foodlink.application.dto.response.BeneficiarioResponse;
import com.foodlink.domain.event.BeneficiarioRegistrado;
import com.foodlink.domain.port.output.IRepositorioBeneficiario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarBeneficiarioUseCaseTest {

    private static final String RUC_VALIDO = "1792146739001";

    @Mock
    private IRepositorioBeneficiario repositorioBeneficiario;

    @Mock
    private ApplicationEventPublisher publicadorEventos;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrarBeneficiarioUseCaseImpl useCase;

    private RegistrarBeneficiarioRequest requestValido() {
        return new RegistrarBeneficiarioRequest("Fundación Manos Unidas", RUC_VALIDO, "contacto@manosunidas.org",
                "0991234567", "Pichincha", "Quito", "Av. 6 de Diciembre N32-100", "Av. Patria", "Junto al parque",
                "SuperClave123");
    }

    @Test
    void deberiaRegistrarBeneficiarioYRetornarResponseConDatosCorrectos() {
        when(repositorioBeneficiario.existePorRuc(RUC_VALIDO)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(repositorioBeneficiario.guardar(any(), any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        BeneficiarioResponse response = useCase.registrar(requestValido());

        assertEquals(RUC_VALIDO, response.ruc());
        assertEquals("Fundación Manos Unidas", response.nombre());
        assertEquals("PENDIENTE", response.estadoVerificacion());
        verify(publicadorEventos).publishEvent(any(BeneficiarioRegistrado.class));
    }

    @Test
    void deberiaFallarAlRegistrarBeneficiarioConRucDuplicado() {
        when(repositorioBeneficiario.existePorRuc(RUC_VALIDO)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.registrar(requestValido()));

        verify(repositorioBeneficiario, never()).guardar(any(), any());
    }
}