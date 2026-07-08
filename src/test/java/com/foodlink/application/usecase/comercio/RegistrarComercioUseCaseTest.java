package com.foodlink.application.usecase.comercio;

import com.foodlink.application.dto.request.RegistrarComercioRequest;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.event.ComercioRegistrado;
import com.foodlink.domain.port.output.IRepositorioComercio;
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
class RegistrarComercioUseCaseTest {

    private static final String RUC_VALIDO = "1792146739001";

    @Mock
    private IRepositorioComercio repositorioComercio;

    @Mock
    private ApplicationEventPublisher publicadorEventos;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrarComercioUseCaseImpl useCase;

    private RegistrarComercioRequest requestValido() {
        return new RegistrarComercioRequest(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", "Pichincha", "Quito", "Av. Amazonas N34-451", "Av. Naciones Unidas",
                "Cerca del parque", "SuperClave123");
    }

    @Test
    void deberiaRegistrarComercioYRetornarResponseConDatosCorrectos() {
        when(repositorioComercio.existePorRuc(RUC_VALIDO)).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(repositorioComercio.guardar(any(), any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        ComercioResponse response = useCase.registrar(requestValido());

        assertEquals(RUC_VALIDO, response.ruc());
        assertEquals("Supermercado El Ahorro", response.nombre());
        assertEquals("PENDIENTE_VERIFICACION", response.estado());
        verify(publicadorEventos).publishEvent(any(ComercioRegistrado.class));
    }

    @Test
    void deberiaFallarAlRegistrarComercioConRucDuplicado() {
        when(repositorioComercio.existePorRuc(RUC_VALIDO)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.registrar(requestValido()));

        verify(repositorioComercio, never()).guardar(any(), any());
    }
}