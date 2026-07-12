package com.foodlink.application.usecase.admin;

import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.domain.model.comercio.exception.ComercioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.output.IRepositorioComercio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrarComercioUseCaseTest {

    private static final String RUC = "1792146739001";

    @Mock
    private IRepositorioComercio repositorioComercio;

    @InjectMocks
    private AdministrarComercioUseCaseImpl useCase;

    private void mockGuardarEcheandoElMismoComercio() {
        when(repositorioComercio.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", null, null);
    }

    private Comercio comercioEnEstado(EstadoComercio estado) {
        Comercio comercio = Comercio.crear(RUC, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
        if (estado == EstadoComercio.VERIFICADO) {
            comercio.verificar();
        } else if (estado == EstadoComercio.RECHAZADO) {
            comercio.rechazar("motivo");
        } else if (estado == EstadoComercio.SUSPENDIDO) {
            comercio.verificar();
            comercio.suspender();
        }
        return comercio;
    }

    @Test
    void verificarConComercioPendienteDeberiaRetornarComercioResponseConEstadoVerificado() {
        Comercio comercio = comercioEnEstado(EstadoComercio.PENDIENTE_VERIFICACION);
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));
        mockGuardarEcheandoElMismoComercio();

        ComercioResponse response = useCase.verificar(comercio.getId());

        assertEquals(EstadoComercio.VERIFICADO.name(), response.estado());
    }

    @Test
    void verificarConComercioNoEncontradoDeberiaLanzarIllegalArgumentException() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.verificar(comercioId));
    }

    @Test
    void verificarConComercioYaVerificadoDeberiaLanzarComercioInvalidoException() {
        Comercio comercio = comercioEnEstado(EstadoComercio.VERIFICADO);
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));

        assertThrows(ComercioInvalidoException.class, () -> useCase.verificar(comercio.getId()));
    }

    @Test
    void rechazarConComercioPendienteDeberiaRetornarComercioResponseConEstadoRechazado() {
        Comercio comercio = comercioEnEstado(EstadoComercio.PENDIENTE_VERIFICACION);
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));
        mockGuardarEcheandoElMismoComercio();

        ComercioResponse response = useCase.rechazar(comercio.getId());

        assertEquals(EstadoComercio.RECHAZADO.name(), response.estado());
    }

    @Test
    void suspenderConComercioVerificadoDeberiaRetornarComercioResponseConEstadoSuspendido() {
        Comercio comercio = comercioEnEstado(EstadoComercio.VERIFICADO);
        when(repositorioComercio.buscarPorId(comercio.getId())).thenReturn(Optional.of(comercio));
        mockGuardarEcheandoElMismoComercio();

        ComercioResponse response = useCase.suspender(comercio.getId());

        assertEquals(EstadoComercio.SUSPENDIDO.name(), response.estado());
    }

    @Test
    void listarPendientesDeberiaLlamarBuscarPorEstadoPendienteVerificacion() {
        when(repositorioComercio.buscarPorEstado(EstadoComercio.PENDIENTE_VERIFICACION)).thenReturn(List.of());

        useCase.listarPendientes();

        verify(repositorioComercio, times(1)).buscarPorEstado(EstadoComercio.PENDIENTE_VERIFICACION);
    }

    @Test
    void listarTodosDeberiaLlamarBuscarTodos() {
        when(repositorioComercio.buscarTodos()).thenReturn(List.of());

        useCase.listarTodos();

        verify(repositorioComercio, times(1)).buscarTodos();
    }
}