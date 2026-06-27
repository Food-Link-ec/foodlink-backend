package com.foodlink.application.usecase.comprador;

import com.foodlink.application.dto.request.RegistrarCompradorRequest;
import com.foodlink.application.dto.response.CompradorResponse;
import com.foodlink.domain.event.CompradorRegistrado;
import com.foodlink.domain.port.output.IRepositorioComprador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarCompradorUseCaseTest {

    private static final String CEDULA_VALIDA = "1710034065";

    @Mock
    private IRepositorioComprador repositorioComprador;

    @Mock
    private ApplicationEventPublisher publicadorEventos;

    @InjectMocks
    private RegistrarCompradorUseCaseImpl useCase;

    private RegistrarCompradorRequest requestValido() {
        return new RegistrarCompradorRequest(CEDULA_VALIDA, "Juan", "Pérez", "juan.perez@mail.com", "0991234567");
    }

    @Test
    void deberiaRegistrarCompradorYRetornarResponseConDatosCorrectos() {
        when(repositorioComprador.existePorCedula(CEDULA_VALIDA)).thenReturn(false);
        when(repositorioComprador.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        CompradorResponse response = useCase.registrar(requestValido());

        assertEquals(CEDULA_VALIDA, response.cedula());
        assertEquals("Juan", response.nombre());
        assertTrue(response.activo());
        verify(publicadorEventos).publishEvent(any(CompradorRegistrado.class));
    }

    @Test
    void deberiaFallarAlRegistrarCompradorConCedulaDuplicada() {
        when(repositorioComprador.existePorCedula(CEDULA_VALIDA)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.registrar(requestValido()));

        verify(repositorioComprador, never()).guardar(any());
    }
}